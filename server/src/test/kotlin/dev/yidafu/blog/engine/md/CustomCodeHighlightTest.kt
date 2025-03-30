package dev.yidafu.blog.dev.yidafu.blog.engine.md

import dev.yidafu.blog.engine.md.CustomCodeHighlight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class CustomCodeHighlightTest : StringSpec({

  "generateCodeHighlight with empty code returns empty HTML" {
    val code = ""
    val language = "java"
    val expectedHtml = ""
    val actualHtml = CustomCodeHighlight.generateCodeHighlight(code, language)
    actualHtml shouldBe expectedHtml
  }

  "generateCodeHighlight with unsupported language defaults to generic syntax" {
    val code = "print()"
    val language = "unknown"
    val expectedHtml = """
      <span style="color: #a626a4">print</span><span style="color: #526fff">(</span><span style="color: #526fff">)</span>
    """.trimIndent()
    val actualHtml = CustomCodeHighlight.generateCodeHighlight(code, language)
    actualHtml shouldBe expectedHtml
  }

  "generateCodeHighlight with multiple highlights generates correct HTML" {
    val code = "var a\n\ta++"
    val language = "js"
    val expectedHtml =
      """
        <span style="color: #a626a4">var</span><span>&nbsp;a<br/>
        &nbsp;&nbsp;a</span><span style="color: #526fff">+</span><span>+</span>
      """.trimIndent()
    val actualHtml = CustomCodeHighlight.generateCodeHighlight(code, language)
    actualHtml shouldBe expectedHtml
  }

  "generateCodeHighlight with nested highlights generates correct HTML" {
    val code = "if (true) {\n    print(\"Hello, World!\")\n}"
    val language = "java"
    val expectedHtml = """
      <span style="color: #a626a4">if</span><span>&nbsp;</span><span style="color: #526fff">(</span><span>true</span><span style="color: #526fff">)</span><span>&nbsp;</span><span style="color: #526fff">{</span><span><br/>
      &nbsp;&nbsp;&nbsp;&nbsp;print</span><span style="color: #526fff">(</span><span style="color: #50a14f">"Hello<span style="color: #e45649">,</span>&nbsp;World!"</span><span>&nbsp;World!"</span><span style="color: #526fff">)</span><span><br/>
      </span><span style="color: #526fff">}</span>
    """.trimIndent()
    val actualHtml = CustomCodeHighlight.generateCodeHighlight(code, language)
    actualHtml shouldBe expectedHtml
  }

  "generateCodeHighlight with javascript highlights generates correct HTML" {
    val code = """
// jQuery 官网示例
var hiddenBox = ${'$'}( "#banner-message" );
${'$'}( "#button-container button" ).on( "click", function( event ) {
  hiddenBox.show();
});
    """.trimIndent()
    val language = "javascript"
    val expectedHtml = """
<span style="color: #a1a1a1">//&nbsp;jQuery&nbsp;官网示例</span><span><br/>
</span><span style="color: #a626a4">var</span><span>&nbsp;hiddenBox&nbsp;</span><span style="color: #526fff">=</span><span>&nbsp;${'$'}</span><span style="color: #526fff">(</span><span>&nbsp;</span><span style="color: #50a14f">"<span style="color: #a1a1a1">#banner<span style="color: #526fff">-</span><span>message"&nbsp;</span><span style="color: #526fff">)</span><span style="color: #e45649">;</span></span><span><br/>
${'$'}</span><span style="color: #526fff">(</span><span>&nbsp;"</span><span style="color: #a1a1a1">#button<span style="color: #526fff">-</span><span>container&nbsp;button"&nbsp;</span><span style="color: #526fff">)</span><span style="color: #e45649">.</span><span>on</span><span style="color: #526fff">(</span><span>&nbsp;"click"</span><span style="color: #e45649">,</span><span>&nbsp;function</span><span style="color: #526fff">(</span><span>&nbsp;event&nbsp;</span><span style="color: #526fff">)</span><span>&nbsp;</span><span style="color: #526fff">{</span></span><span><br/>
&nbsp;&nbsp;hiddenBox</span><span style="color: #e45649">.</span><span>show</span><span style="color: #526fff">(</span><span style="color: #526fff">)</span><span style="color: #e45649">;</span><span><br/>
</span><span style="color: #526fff">}</span><span style="color: #526fff">)</span><span style="color: #e45649">;</span>
    """.trimIndent()

    val actualHtml = CustomCodeHighlight.generateCodeHighlight(code, language)
    actualHtml shouldBe expectedHtml
  }
})
