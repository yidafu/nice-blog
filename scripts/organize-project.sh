#!/bin/bash
# 项目目录结构优化脚本

set -e

echo "🔧 开始优化项目目录结构..."

# 1. 移动进度报告文档到 docs/progress/
echo "📁 整理进度文档..."
mv -f KMP*.md docs/progress/ 2>/dev/null || true
mv -f *MIGRATION*.md docs/progress/ 2>/dev/null || true
mv -f *REFACTORING*.md docs/progress/ 2>/dev/null || true
mv -f *REMOVAL*.md docs/progress/ 2>/dev/null || true
mv -f *MERGE*.md docs/progress/ 2>/dev/null || true
mv -f BUILD_FIX_SUMMARY.md docs/progress/ 2>/dev/null || true

# 2. 移动指南文档到 docs/guides/
echo "📚 整理指南文档..."
mv -f CONTRIBUTING.md docs/guides/ 2>/dev/null || true
mv -f FEISHU*.md docs/guides/ 2>/dev/null || true
mv -f FILE_SOURCE_SUPPORT.md docs/guides/ 2>/dev/null || true
mv -f PROJECT_*.md docs/guides/ 2>/dev/null || true
mv -f QUICKSTART*.md docs/guides/ 2>/dev/null || true
mv -f README_STATIC_GENERATOR.md docs/guides/ 2>/dev/null || true
mv -f STATIC_GENERATOR.md docs/guides/ 2>/dev/null || true
mv -f TESTING.md docs/guides/ 2>/dev/null || true
mv -f SCRIPTS.md docs/guides/ 2>/dev/null || true
mv -f NAMING.md docs/guides/ 2>/dev/null || true
mv -f PR.md docs/guides/ 2>/dev/null || true
mv -f NICEMAKER_SRC_RESTRUCTURE.md docs/guides/ 2>/dev/null || true
mv -f PACKAGE*.md docs/guides/ 2>/dev/null || true

# 3. 移动脚本文件到 scripts/
echo "🔨 整理脚本文件..."
mv -f *.sh scripts/ 2>/dev/null || true
mv -f maker scripts/ 2>/dev/null || true
chmod +x scripts/*.sh 2>/dev/null || true
chmod +x scripts/maker 2>/dev/null || true

# 4. 统一示例博客到 examples/
echo "📝 整理示例博客..."
# 保留 example-blog，删除重复的
if [ -d "example-blog" ]; then
  mv -f example-blog examples/blog 2>/dev/null || true
fi
rm -rf exmaple-blog 2>/dev/null || true

# sample-blog 如果不同则保留
if [ -d "sample-blog" ]; then
  if [ ! -d "examples/sample-blog" ]; then
    mv -f sample-blog examples/ 2>/dev/null || true
  else
    rm -rf sample-blog 2>/dev/null || true
  fi
fi

# 5. 移动临时和测试文件到 .tmp/
echo "🗑️  清理临时文件..."
mv -f local.* .tmp/ 2>/dev/null || true
mv -f test-config*.yaml .tmp/ 2>/dev/null || true
mv -f *.db.* .tmp/ 2>/dev/null || true

# 6. 移动随机 HTML 文件到 .tmp/
echo "🧹 清理随机文件..."
mv -f React*.html .tmp/ 2>/dev/null || true
mv -f *.ipynb.html .tmp/ 2>/dev/null || true

# 7. 移动 uuid 和其他测试目录到 .tmp/
if [ -d "uuid" ]; then
  mv -f uuid .tmp/ 2>/dev/null || true
fi

# 8. 清理空目录
echo "🧼 清理空目录..."
find . -type d -empty -depth -delete 2>/dev/null || true

echo ""
echo "✅ 目录结构优化完成！"
echo ""
echo "📊 新的目录结构："
echo "  docs/"
echo "    ├── progress/     # 项目进度文档"
echo "    └── guides/       # 使用指南"
echo "  scripts/            # 所有脚本文件"
echo "  examples/           # 示例博客"
echo "  .tmp/               # 临时文件（已添加到 .gitignore）"
echo ""
echo "⚠️  注意："
echo "  - output/ 目录保留（构建输出）"
echo "  - logs/ 目录建议添加到 .gitignore"
echo "  - kotlin-js-store/ 是构建缓存"
echo "  - archive/ 包含旧代码，可以考虑删除"
echo ""

