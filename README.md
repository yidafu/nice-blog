# NiceMaker 📦

> A modern Kotlin static blog generator

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![Ktor](https://img.shields.io/badge/Ktor-3.3.0-087CFA.svg?logo=ktor)](https://ktor.io)
[![KMP](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF.svg?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Platform](https://img.shields.io/badge/Platform-JVM%20%7C%20JS%20%7C%20Native-brightgreen.svg)](https://kotlinlang.org/docs/multiplatform.html)
[![Version](https://img.shields.io/badge/Version-0.1.0-orange.svg)](https://github.com/yidafu/nice-blog/releases)
[![Language](https://img.shields.io/badge/Language-English-blue.svg)](./README.md)

**Config File**: `nice.yaml` | **CLI Tool**: `maker`

## ✨ Features

- 📝 Multi-format Support: Markdown, Jupyter Notebook, Feishu Docs
- 🏷️ Tag System: Automatic tag aggregation and smart categorization
- 📚 Series Organization: Organize articles by series with automatic ordering
- 👤 About Page: Auto-generated from AboutMe.md
- ⚡ Fast Build: High-performance processing with Kotlin
- 🎨 Modern UI: Ktor HTML DSL + Responsive Design
- 🌍 Internationalization: Chinese and English support
- 🚀 Zero-Cost Deployment: GitHub Pages, Cloudflare Pages, Netlify
- 🔧 Easy to Use: Configuration as code, pure static generation

## 🚀 Quick Start

### 1. Configuration

```bash
cp nice.example.yaml nice.yaml
```

Edit `nice.yaml`:

```yaml
site:
  title: "My Blog"
  url: "https://myblog.com"

content:
  source:
    url: "https://github.com/username/blog-content.git"
```

### 2. Build

```bash
./maker build
```

### 3. Preview

```bash
./maker serve
```

Visit http://localhost:3000

### 4. Deploy

```bash
# GitHub Pages
cd output
git init
git add .
git commit -m "Deploy"
git push -f git@github.com:username/username.github.io.git main
```

## 📖 Commands

```bash
./maker build              # Generate static site
./maker build -v           # Verbose output
./maker serve              # Preview (port 3000)
./maker serve 8080         # Specify port
./maker new "Article Title"      # Create Markdown article
./maker new "Title" -t feishu    # Create Feishu doc reference
```

## 📝 Supported Formats

### Markdown

```markdown
---
title: My Article
cover: cover.jpg
description: Summary
tags: ["Kotlin", "Web Development", "Tutorial"]
series: "Kotlin Getting Started"
seriesOrder: 1
---

# My Article

Content...
```

**Front Matter Fields**:
- `title`: Article title (required)
- `cover`: Cover image
- `description`: Summary description
- `tags`: Tag list (auto-generates tag pages)
- `series`: Series name (auto-generates series pages)
- `seriesOrder`: Order within series (starting from 1)

### Jupyter Notebook

`.ipynb` files, with front matter in the first cell

### Feishu Docs

Create a `.feishu.yaml` file:

```yaml
docxId: "TPDddjY5foJZ8axlf9fctf2Wnse"
title: "Feishu Article"
cover: "cover.png"
```

## ⚙️ Configuration File

`nice.yaml` configuration:

```yaml
site:
  title: "Site Title"
  url: "https://example.com"
  language: en-US

content:
  source:
    url: "git repository URL"
    branch: "main"
    localPath: "/path/to/cache"

  feishu:  # Optional
    enabled: true
    appId: "${FEISHU_APP_ID}"
    appSecret: "${FEISHU_APP_SECRET}"

build:
  output: "./output"
  cleanBeforeBuild: true
  incremental: false

pagination:
  pageSize: 10

theme:
  name: "simple"  # Options: "simple", "blank"
```

## 🎨 Page Features

### 📑 Tags Page
Tags are automatically extracted from article front matter, no manual configuration needed.

Generated pages:
- `/tags.html` - Tag list showing all tags and article counts

Usage in articles:
```markdown
---
tags: ["Kotlin", "Web Development", "Tutorial"]
---
```

### 📚 Series Pages
Series feature helps organize related articles with automatic ordering.

Generated pages:
- `/series.html` - Series list
- `/series/{name}.html` - Individual series detail pages

Usage in articles:
```markdown
---
series: "Kotlin Getting Started"
seriesOrder: 1
---
```

Articles in a series are sorted by `seriesOrder`.

### 👤 About Page
Create an `AboutMe.md` file in the Git repository root:

```markdown
# About Me

Hello! I am...

## Tech Stack
- Kotlin
- ...

## Contact
- Email: example@email.com
```

Generated page:
- `/about.html` - About page

Supports full Markdown syntax including code highlighting, images, etc.

## 🌐 Deployment

### GitHub Pages

```bash
./maker build
cd output
git init && git add . && git commit -m "Deploy"
git push -f git@github.com:user/user.github.io.git main
```

### Cloudflare Pages

- Build command: `./maker build`
- Output directory: `output`

### Vercel / Netlify

- Build Command: `./maker build`
- Publish Directory: `output`

## 📊 Performance

- Small sites (<10 articles): ~2s
- Medium sites (~50 articles): ~10s
- Large sites (100+ articles): ~30s
- Page load: <20ms (CDN)

## 🛠️ Tech Stack

- Kotlin 2.1.20 (Multiplatform)
- Ktor Server CIO
- kotlinx-cli 0.3.6
- kotlin-logging (KMP version)
- jetbrains-markdown
- jupyter-notebooks-parser
- feishu2html 1.0.2

## 🏗️ Project Structure

```
nice-blog/
├── nicemaker/          # Main module (core generator)
│   ├── src/
│   │   ├── commonMain/ # Cross-platform common code
│   │   ├── jvmMain/    # JVM platform code
│   │   ├── jsMain/     # JS platform code
│   │   └── nativeMain/ # Native platform code
│   └── build.gradle.kts
├── themes/             # Theme module (plugin-based)
│   ├── src/commonMain/ # Theme implementation
│   └── build.gradle.kts
├── nice.yaml           # Site configuration file
├── nice.example.yaml   # Configuration example
├── maker               # CLI convenience script
└── output/             # Generated static site
```

## 🎨 Custom Themes

NiceMaker uses a plugin-based theme system that supports custom theme development:

1. Create a new theme module
2. Implement the `TemplateManager` interface
3. Register the theme using ServiceLoader
4. Specify the theme name in `nice.yaml`

See [Theme Development Documentation](./themes/README.md)

## 📚 Documentation

- [Quick Start](./QUICKSTART.md) - 30-second guide
- [Project Overview](./PROJECT_OVERVIEW.md) - Architecture and design philosophy
- [User Guide](./STATIC_GENERATOR.md) - Complete documentation
- [Contributing Guide](./CONTRIBUTING.md) - How to contribute
- [Feishu Integration](./FEISHU_INTEGRATION.md) - Feishu docs integration
- [Migration Summary](./MIGRATION_SUMMARY.md) - Project evolution history

## 🤝 Contributing

We welcome all forms of contributions! Whether it's reporting bugs, suggesting features, improving documentation, or submitting code.

Before contributing, please read our [Contributing Guide](./CONTRIBUTING.md) to learn about:

- 📋 How to report bugs and suggest features
- 🛠️ Development environment setup
- 📏 Code standards and commit conventions
- 🔄 Pull request process

Quick start:

```bash
# Fork and clone the project
git clone https://github.com/your-username/nice-blog.git

# Install dependencies
./gradlew build

# Run tests
./gradlew test

# Code check
./gradlew ktlintCheck
```

For detailed information, see [CONTRIBUTING.md](./CONTRIBUTING.md)

## 📄 License

MIT License

Copyright (c) 2025 NiceMaker

## 📞 Contact

- Project Homepage: https://github.com/yidafu/nice-blog
- Issue Tracker: https://github.com/yidafu/nice-blog/issues

---

**Config**: `nice.yaml` | **Command**: `maker` | **Project**: NiceMaker

💡 Make blogging simpler and more elegant!
