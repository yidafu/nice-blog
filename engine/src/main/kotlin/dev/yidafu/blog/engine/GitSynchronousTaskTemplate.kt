package dev.yidafu.blog.dev.yidafu.blog.engine

import com.charleskorn.kaml.Yaml
import dev.yidafu.blog.common.dto.FrontMatterDTO
import dev.yidafu.blog.common.dto.MarkdownArticleDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalTime
import java.time.ZoneId
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension


abstract class GitSynchronousTaskTemplate(protected val ctx: SyncContext) {
  private val log = LoggerFactory.getLogger(this::class.java)

  class GFMFlavorExtendDescriptor : GFMFlavourDescriptor() {
    override fun createHtmlGeneratingProviders(
      linkMap: LinkMap,
      baseURI: org.intellij.markdown.html.URI?,
    ): Map<IElementType, GeneratingProvider> {
      return super.createHtmlGeneratingProviders(linkMap, baseURI) + hashMapOf(
        MarkdownElementTypes.CODE_FENCE to CodeFenceGeneratingProvider()
      )
    }
  }

  private val flavour = GFMFlavorExtendDescriptor()
  private val parser = MarkdownParser(flavour)

  private fun resolvePath(file: File, url: String): File {
    return Paths.get(file.parentFile.absolutePath, url).toFile()
  }

  private fun parseFrontMatter(markdownFile: File, text: String, tree: ASTNode): FrontMatterDTO? {
    val horizontalRules = tree.findChildrenOfType(MarkdownTokenTypes.HORIZONTAL_RULE)
    if (horizontalRules.size > 1) {
      // markdown file start with `---`
      if (tree.indexOf(horizontalRules[0]) == 0) {
        val secondHorizontalRule = tree.indexOf(MarkdownTokenTypes.HORIZONTAL_RULE, 1)
        val secondHorizontalRuleIdx = tree.indexOf(secondHorizontalRule)
        val frontMatter = tree.slice(1, secondHorizontalRuleIdx)
        frontMatter.forEach { node ->
          if (node is CompositeASTNode) {
            val frontMatterText = node.getTextInNode(text)

            val dto = Yaml.default.decodeFromString<FrontMatterDTO>(frontMatterText.toString())

            dto.rawContent = text.substring(horizontalRules[0].startOffset, secondHorizontalRule.endOffset)

            val coverUrl = updateImage(resolvePath(markdownFile, dto.cover))
//            ctx.log("node content ==> $dto")
            return dto.copy(cover = coverUrl.toString())
          }
        }
      }
    }
    return null
  }

  /**
   * clone or update local repository
   */
  abstract fun fetchRepository(): File

  open fun parseFile(path: Path): MarkdownArticleDTO {
    val markdownFile = path.toFile()
    val text = markdownFile.readText()
    val filename = path.name
    val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)

    val createDate = LocalTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
    val updateDate = LocalTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault())

//    ctx.log("tree type ${tree.type.name}")
    val frontMatterDTO = parseFrontMatter(markdownFile, text, parser.buildMarkdownTreeFromString(text))

    val textWithoutFrontMatter = frontMatterDTO?.rawContent?.let { rawContent ->
      text.replace(rawContent, "")
    } ?: text
//    ctx.log("textWithoutFrontMatter ==> $textWithoutFrontMatter")

    val tree = parser.buildMarkdownTreeFromString(textWithoutFrontMatter)
    MarkdownVisitor(path.toFile(), textWithoutFrontMatter).visitNode(tree)
    val html = HtmlGenerator(textWithoutFrontMatter, tree, flavour).generateHtml()

    val dto = MarkdownArticleDTO(
      filename,
      "",
      frontMatterDTO,
      textWithoutFrontMatter,
      html,
      createDate,
      updateDate,
    )
    return dto
//    ctx.log("markdown article $dto")
  }


  /**
   * upload local image to server
   */
  abstract fun updateImage(img: File): java.net.URI

  /**
   * persistent articles to db/cache etc.
   */
  abstract fun persistentPost(dto: MarkdownArticleDTO)

  abstract fun cleanup()
  suspend fun sync() {
    // execute sync task in io thread
    ctx.onStart()
    try {
      ctx.log("start sync task...")
      val repoDirectory = fetchRepository()
      ctx.log("scan markdown in ${repoDirectory.toPath()}")
      Files.find(
        repoDirectory.toPath(),
        Int.MAX_VALUE,
        { path, file: BasicFileAttributes ->
          file.isRegularFile
            && path.extension == "md"
            && path.nameWithoutExtension != "README"
        })
        .use { paths ->
          paths.forEach { path ->
            ctx.log("reading markdown file $path")
            val markdownDTO = parseFile(path)
            persistentPost(markdownDTO)
          }
        }
      ctx.onFinish()
    } catch (e: Exception) {
      log.error("sync execute failed", e)
      ctx.log("sync task failed: ${e.message}")
      ctx.onFailed()
    } finally {
      cleanup()
    }

  }

  inner class MarkdownVisitor(
    private val markdownFile: File,
    private val markdownText: String,
  ) : RecursiveVisitor() {

    override fun visitNode(node: ASTNode) {
      if (node is CompositeASTNode) {
        if (node.type == MarkdownElementTypes.IMAGE) {
          extractImageLinks(node)
        }

      }
      super.visitNode(node)
    }

    private fun extractImageLinks(node: ASTNode) {
      node.findChildOfType(MarkdownElementTypes.INLINE_LINK)?.let { linKNode ->
        val linkUrl = linKNode.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(markdownText)
        val label = linKNode.findChildOfType(MarkdownElementTypes.LINK_TEXT)?.getTextInNode(markdownText)
        val link = MLink(linkUrl.toString(), label.toString())
        log.info("m link => {}", link)

        val remoteUrl = updateImage(resolvePath(markdownFile, linkUrl.toString()))
      }
    }
  }

  data class MLink(val url: String, val alt: String)
}
