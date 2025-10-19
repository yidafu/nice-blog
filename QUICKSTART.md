# NiceMaker Quick Start 🚀

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.20-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![KMP](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF.svg?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

A modern Kotlin static blog generator

**Key Features**: Tag system, series organization, about page, multi-format support

## 30-Second Quick Start

```bash
# 1. Configuration
cp nice.example.yaml nice.yaml

# 2. Build
./maker build

# 3. Preview
./maker serve
```

Visit http://localhost:3000

Generated pages:
- `/` - Home (article list)
- `/tags.html` - Tag list
- `/series.html` - Series list
- `/about.html` - About page
- `/articles/*.html` - Article details

## Core Commands

```bash
./maker build              # Generate static site
./maker serve              # Preview (port 3000)
./maker serve 8080         # Specify port
./maker new "Article Title"      # Create Markdown
./maker new "Title" -t feishu    # Create Feishu reference
```

## Configuration File

`nice.yaml`:

```yaml
site:
  title: "My Blog"
  url: "https://myblog.com"

content:
  source:
    url: "https://github.com/user/blog-content.git"
```

## Supported Formats

### 📝 Markdown

```markdown
---
title: Title
cover: cover.jpg
description: Summary
tags: ["Kotlin", "Web Development"]
series: "Getting Started"
seriesOrder: 1
---

# Content

Body...
```

**New Fields**:
- `tags` - Tag list, auto-generates /tags.html
- `series` - Series name, auto-generates /series.html
- `seriesOrder` - Order within series (starting from 1)

### 📓 Jupyter Notebook

`.ipynb` files, with YAML front matter in the first cell

### 📱 Feishu Docs

Create a `.feishu.yaml`:

```yaml
docxId: "Document ID"
title: "Title"
```

### 👤 About Page

Create `AboutMe.md` in the Git repository root:

```markdown
# About Me

Hello! I am a full-stack developer.

## Tech Stack
- Kotlin, Java
- React, Vue

## Contact
- Email: me@example.com
- GitHub: https://github.com/username
```

This will automatically generate the `/about.html` page.

## Deployment

### GitHub Pages

```bash
./maker build
cd output
git init && git add . && git commit -m "Deploy"
git push -f git@github.com:user/user.github.io.git main
```

### Cloudflare Pages

- Build: `./maker build`
- Output: `output`

## Project Info

- **Project**: NiceMaker
- **Package**: dev.yidafu.nicemaker.*
- **Config**: nice.yaml
- **Command**: maker
- **Docs**: [Full Documentation](./STATIC_GENERATOR.md)

## Tech Stack

- Kotlin 2.1.20 (Multiplatform)
- kotlinx-cli 0.3.6
- kotlin-logging (KMP version)
- jetbrains-markdown
- jupyter-notebooks-parser
- feishu2html 1.0.2

## Learn More

- [Full Documentation](./README.md) - Project intro and complete features
- [Project Overview](./PROJECT_OVERVIEW.md) - Architecture and design philosophy
- [Feishu Integration](./FEISHU_INTEGRATION.md) - Feishu docs integration guide

## 🤝 Contributing

If you want to contribute to the NiceMaker project (fix bugs, add features, improve docs, etc.), please see:

- [Contributing Guide](./CONTRIBUTING.md) - Dev environment, code standards, PR process

---

**Start creating your beautiful blog!** ✨
