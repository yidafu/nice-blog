# Contributing Guide 🤝

Thank you for your interest in the NiceMaker project! We welcome all forms of contributions.

> **📌 Important Note**
>
> - If you want to **use NiceMaker** to generate a blog, please see the [Quick Start Guide](./QUICKSTART.md)
> - If you want to **contribute code to NiceMaker**, continue reading this document

This document is for developers who want to contribute code, fix bugs, or improve features for the NiceMaker project.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How to Contribute](#how-to-contribute)
- [Development Environment Setup](#development-environment-setup)
- [Code Standards](#code-standards)
- [Commit Conventions](#commit-conventions)
- [Pull Request Process](#pull-request-process)
- [Bug Reports](#bug-reports)
- [Feature Requests](#feature-requests)
- [Debugging Tips](#debugging-tips)
- [Testing](#testing)
- [Development Resources](#development-resources)

## 📜 Code of Conduct

By participating in this project, you agree to abide by our code of conduct:

- Respect all participants
- Accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## 🚀 How to Contribute

You can contribute to the project in the following ways:

### 1. Report Bugs

If you find a bug, please:
- Search [Issues](https://github.com/yidafu/nice-blog/issues) to confirm it hasn't been reported
- Create a new Issue with:
  - Clear title and description
  - Steps to reproduce
  - Expected and actual behavior
  - System environment info (OS, Kotlin version, etc.)
  - Relevant logs or screenshots

### 2. Suggest Features

If you have ideas for new features:
- Search [Issues](https://github.com/yidafu/nice-blog/issues) to confirm it doesn't exist
- Create a new Issue tagged as `enhancement`
- Describe the feature requirements and use cases in detail
- If possible, provide examples or design sketches

### 3. Improve Documentation

Documentation improvements are very important:
- Fix spelling or grammar errors
- Add missing explanations
- Add usage examples
- Translate documentation to other languages

### 4. Contribute Code

Code improvements and new features are welcome!

## 🛠️ Development Environment Setup

### Prerequisites

Before you start contributing, make sure you have:

1. Read the [Quick Start Guide](./QUICKSTART.md) and understand how to use NiceMaker
2. Familiar with basic Git operations
3. Knowledge of Kotlin programming language

### Environment Requirements

- **JDK**: 17 or higher
- **Kotlin**: 2.1.20 (automatically used by the project)
- **Gradle**: 8.x (using wrapper, no manual installation needed)
- **Git**: For version control
- **IDE**: IntelliJ IDEA or Android Studio recommended

### Clone the Project

```bash
# Clone your fork
git clone https://github.com/your-username/nice-blog.git
cd nice-blog

# Add upstream repository
git remote add upstream https://github.com/yidafu/nice-blog.git

# Install dependencies and build
./gradlew build
```

### Project Structure

```
nice-blog/
├── nicemaker/          # Main module (core generator)
│   ├── src/
│   │   ├── commonMain/ # Cross-platform common code (core logic)
│   │   ├── jvmMain/    # JVM platform code (CLI, file handling)
│   │   ├── jsMain/     # JS platform code (browser features)
│   │   └── nativeMain/ # Native platform code (native binaries)
│   └── build.gradle.kts
├── themes/             # Theme module (plugin-based)
│   ├── src/commonMain/ # Theme implementation (Simple, Blank)
│   └── build.gradle.kts
├── sample-blog/        # Example blog content
├── output/             # Build output directory
└── docs/               # Documentation files
```

### Development Workflow

```bash
# 1. Create feature branch
git checkout -b feature/your-feature-name

# 2. Make code changes
# ...

# 3. Run code checks
./gradlew ktlintCheck

# 4. Run tests
./gradlew test

# 5. Test CLI locally
./gradlew :nicemaker:installDist
./maker build
./maker serve

# 6. Commit changes
git add .
git commit -m "feat: add new feature"

# 7. Push to your fork
git push origin feature/your-feature-name
```

## 📏 Code Standards

### Kotlin Code Style

We use **ktlint** for code formatting and checking.

#### Run ktlint

```bash
# Check code style
./gradlew ktlintCheck

# Auto-format code
./gradlew ktlintFormat
```

#### Main Conventions

- **Indentation**: Use 4 spaces, not tabs
- **Naming**:
  - Class names: `PascalCase` (e.g., `StaticSiteGenerator`)
  - Functions and variables: `camelCase` (e.g., `generateSite`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `MAX_PAGE_SIZE`)
  - Package names: All lowercase (e.g., `dev.yidafu.nicemaker`)
- **Imports**: Avoid wildcard imports (`import foo.*`)
- **Documentation**: Public APIs must have KDoc comments

#### KDoc Example

```kotlin
/**
 * Generate static site
 *
 * @param config Site configuration
 * @param outputDir Output directory
 * @return Number of generated files
 * @throws IOException When file operations fail
 */
fun generateSite(config: SiteConfig, outputDir: String): Int {
    // Implementation...
}
```

### Code Quality

- ✅ Follow SOLID principles
- ✅ Keep functions short (typically no more than 30 lines)
- ✅ Avoid deep nesting (maximum 3 levels)
- ✅ Use meaningful variable names
- ✅ Add necessary comments
- ✅ Handle edge cases

## 📝 Commit Conventions

We use the **Conventional Commits** specification:

### Commit Message Format

```
<type>(<scope>): <short description>

<detailed description> (optional)

<footer> (optional)
```

### Commit Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation update
- `style`: Code formatting (no functional changes)
- `refactor`: Code refactoring
- `perf`: Performance optimization
- `test`: Test-related
- `chore`: Build tool or auxiliary tool changes
- `ci`: CI/CD related

### Examples

```bash
# New feature
git commit -m "feat(generator): add RSS feed support"

# Bug fix
git commit -m "fix(markdown): fix code block highlighting issue"

# Documentation update
git commit -m "docs(readme): update installation instructions"

# Refactoring
git commit -m "refactor(theme): optimize theme loading logic"
```

## 🔄 Pull Request Process

### 1. Fork the Project

Click the "Fork" button in the upper right corner of the GitHub page

### 2. Create Branch

```bash
# Clone your fork
git clone https://github.com/your-username/nice-blog.git
cd nice-blog

# Add upstream repository
git remote add upstream https://github.com/yidafu/nice-blog.git

# Create feature branch
git checkout -b feature/your-feature-name
```

### 3. Develop and Commit

```bash
# Make code changes
# ...

# Run tests
./gradlew test

# Run code checks
./gradlew ktlintCheck

# Commit changes
git add .
git commit -m "feat: add new feature"
```

### 4. Keep in Sync

```bash
# Fetch upstream updates
git fetch upstream

# Rebase with upstream main branch
git rebase upstream/main
```

### 5. Push to Fork

```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

1. Visit your fork page
2. Click "New Pull Request"
3. Fill in the PR description:
   - Explain the changes
   - Link related Issues (e.g., `Fixes #123`)
   - Add test instructions
   - Include screenshots (if there are UI changes)

### PR Checklist

Before submitting a PR, make sure:

- [ ] Code passes `./gradlew test`
- [ ] Code passes `./gradlew ktlintCheck`
- [ ] Added necessary tests
- [ ] Updated relevant documentation
- [ ] Commit messages follow conventions
- [ ] No new warnings introduced
- [ ] Code has been self-reviewed

## 🐛 Bug Reports

### Bug Report Template

```markdown
**Describe the Bug**
Brief description of the bug

**Steps to Reproduce**
1. Run command '...'
2. Modify config '...'
3. Check output '...'
4. See error

**Expected Behavior**
Describe what you expected to happen

**Actual Behavior**
Describe what actually happened

**Environment**
- OS: [e.g., macOS 13.0]
- Kotlin Version: [e.g., 2.1.20]
- NiceMaker Version: [e.g., 0.1.0]
- JDK Version: [e.g., 17]

**Logs**
```
Paste relevant logs
```

**Screenshots**
If applicable, add screenshots
```

## 💡 Feature Requests

### Feature Request Template

```markdown
**Feature Description**
Brief description of the suggested feature

**Use Case**
Describe why this feature is needed

**Suggested Implementation**
If you have ideas, describe how to implement it

**Alternatives**
Have you considered other approaches?

**Additional Information**
Other supplementary information
```

## 🔍 Debugging Tips

### Local Debugging

```bash
# Run CLI using Gradle (convenient for debugging)
./gradlew :nicemaker:run --args="build -v"

# View detailed logs
export LOG_LEVEL=DEBUG
./maker build
```

### IDE Debugging

In IntelliJ IDEA:

1. Open `nicemaker/src/jvmMain/kotlin/dev/yidafu/nicemaker/cli/GeneratorCLI.kt`
2. Set breakpoint in `main` function
3. Create Run Configuration:
   - Main class: `dev.yidafu.nicemaker.cli.GeneratorCLIKt`
   - Program arguments: `build -v`
4. Click Debug button

### Common Issues

#### Build Failures

```bash
# Clean and rebuild
./gradlew clean build

# Refresh dependencies
./gradlew build --refresh-dependencies
```

#### ktlint Check Failures

```bash
# Auto-fix most formatting issues
./gradlew ktlintFormat

# If issues remain, fix manually and recheck
./gradlew ktlintCheck
```

#### Test Failures

```bash
# Run single test
./gradlew :nicemaker:test --tests "ClassName.testMethodName"

# View detailed test report
open nicemaker/build/reports/tests/test/index.html
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :nicemaker:test
./gradlew :themes:test

# View test report
open nicemaker/build/reports/tests/test/index.html
```

### Writing Tests

```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals

class MyFeatureTest {
    @Test
    fun `should generate correct output`() {
        // Given
        val input = "test"

        // When
        val result = myFeature(input)

        // Then
        assertEquals("expected", result)
    }
}
```

## 📚 Development Resources

### Technical Documentation

- [Kotlin Official Docs](https://kotlinlang.org/docs/home.html)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Ktor Documentation](https://ktor.io/docs/)
- [kotlinx.html](https://github.com/Kotlin/kotlinx.html)

### Project Documentation

- [README](./README.md) - Project introduction
- [QUICKSTART](./QUICKSTART.md) - Quick start
- [PROJECT_OVERVIEW](./PROJECT_OVERVIEW.md) - Project overview
- [STATIC_GENERATOR](./STATIC_GENERATOR.md) - User guide
- [FEISHU_INTEGRATION](./FEISHU_INTEGRATION.md) - Feishu integration

## 🎯 Roadmap

Check our [Project Board](https://github.com/yidafu/nice-blog/projects) for current development progress and plans.

## ❓ Getting Help

If you encounter issues while contributing:

- Check the [FAQ](https://github.com/yidafu/nice-blog/wiki/FAQ)
- Search existing [Issues](https://github.com/yidafu/nice-blog/issues)
- Ask questions in [Discussions](https://github.com/yidafu/nice-blog/discussions)
- Contact maintainers: [@yidafu in Issues](https://github.com/yidafu/nice-blog/issues)

## 🌟 Contributors

Thanks to everyone who has contributed to NiceMaker!

<!-- Contributors will be automatically displayed here -->
<a href="https://github.com/yidafu/nice-blog/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=yidafu/nice-blog" />
</a>

## 📄 License

By contributing to this project, you agree that your contributions will be licensed under the [MIT License](./LICENSE).

---

**Thank you for your contribution!** 💖

If you're interested in NiceMaker, please give us a ⭐️!
