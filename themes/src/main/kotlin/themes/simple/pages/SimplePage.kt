package dev.yidafu.blog.themes.simple.pages

import de.comahe.i18n4k.Locale
import de.comahe.i18n4k.messages.MessageBundleLocalizedString
import dev.yidafu.blog.themes.DataModal
import dev.yidafu.blog.themes.Page
import kotlinx.html.*

open class SimplePage(final override val modal: DataModal) : Page {
  protected open val headBlock: HEAD.() -> Unit = {}

  protected open val bodyBlock: BODY.() -> Unit = {}

  protected val locale: Locale = modal.locale

  protected val currentPath: String = modal.path
  protected val siteTitle: String = modal.siteTitle
  protected val githubUrl: String = modal.githubUrl

  protected fun MessageBundleLocalizedString.toText(): String {
    return toString(locale)
  }

  override fun render(html: HTML) {
    html.apply {
      head {
        link {
          rel = "stylesheet"
          href = "/public/normalize.css"
        }
        link {
          rel = "shortcut icon"
          href = "/public/favicon.ico"
        }
        meta { charset = "UTF-8" }
        title {
          +modal.siteTitle
        }

        style {
          unsafe {
            +defaultHeadStyle.toString()
            raw("""
              /* 布局工具类 */
              .flex { display: flex; }
              .flex-col { flex-direction: column; }
              .flex-row { flex-direction: row; }
              .flex-wrap { flex-wrap: wrap; }
              .flex-grow { flex-grow: 1; }
              .flex-fill { flex: 1 1 auto; }
              .flex-shrink-0 { flex-shrink: 0; }
              .justify-start { justify-content: flex-start; }
              .justify-center { justify-content: center; }
              .justify-between { justify-content: space-between; }
              .justify-end { justify-content: flex-end; }
              .items-start { align-items: flex-start; }
              .items-center { align-items: center; }
              .items-end { align-items: flex-end; }

              /* 间距工具类 */
              .p-2 { padding: 0.5rem; }
              .p-3 { padding: 0.75rem; }
              .p-4 { padding: 1rem; }
              .p-6 { padding: 1.5rem; }
              .p-8 { padding: 2rem; }
              .px-2 { padding-left: 0.5rem; padding-right: 0.5rem; }
              .px-3 { padding-left: 0.75rem; padding-right: 0.75rem; }
              .px-4 { padding-left: 1rem; padding-right: 1rem; }
              .px-5 { padding-left: 1.25rem; padding-right: 1.25rem; }
              .px-6 { padding-left: 1.5rem; padding-right: 1.5rem; }
              .px-16 { padding-left: 4rem; padding-right: 4rem; }
              .py-2 { padding-top: 0.5rem; padding-bottom: 0.5rem; }
              .py-3 { padding-top: 0.75rem; padding-bottom: 0.75rem; }
              .py-4 { padding-top: 1rem; padding-bottom: 1rem; }
              .py-6 { padding-top: 1.5rem; padding-bottom: 1.5rem; }
              .py-8 { padding-top: 2rem; padding-bottom: 2rem; }

              .m-2 { margin: 0.5rem; }
              .m-4 { margin: 1rem; }
              .m-6 { margin: 1.5rem; }
              .m-8 { margin: 2rem; }
              .mx-2 { margin-left: 0.5rem; margin-right: 0.5rem; }
              .mx-3 { margin-left: 0.75rem; margin-right: 0.75rem; }
              .mx-4 { margin-left: 1rem; margin-right: 1rem; }
              .my-3 { margin-top: 0.75rem; margin-bottom: 0.75rem; }
              .my-4 { margin-top: 1rem; margin-bottom: 1rem; }
              .my-8 { margin-top: 2rem; margin-bottom: 2rem; }
              .my-16 { margin-top: 4rem; margin-bottom: 4rem; }
              .mt-4 { margin-top: 1rem; }
              .mt-5 { margin-top: 1.25rem; }
              .mt-16 { margin-top: 4rem; }
              .mt-20 { margin-top: 5rem; }
              .mb-16 { margin-bottom: 4rem; }
              .mr-2 { margin-right: 0.5rem; }
              .mr-3 { margin-right: 0.5rem; }
              .mr-4 { margin-right: 1rem; }
              .mr-6 { margin-right: 1.5rem; }
              .mr-20 { margin-right: 5rem; }
              .ml-4 { margin-left: 1rem; }

              /* 尺寸工具类 */
              .w-full { width: 100%; }
              .w-60 { width: 15rem; }
              .w-75 { width: 18.75rem; }
              .w-100 { width: 25rem; }
              .w-200 { width: 50rem; }
              .w-256 { width: 64rem; }
              .w-300 { width: 75rem; }
              .h-5 { height: 1.25rem; }
              .h-6 { height: 1.5rem; }
              .h-8 { height: 2rem; }
              .h-10 { height: 2.5rem; }
              .h-12 { height: 3rem; }
              .h-15 { height: 3.75rem; }
              .h-80 { height: 20rem; }
              .max-w-256 { max-width: 64rem; }
              .min-w-40 { min-width: 10rem; }
              .min-h-screen { min-height: 100vh; }

              /* 文本工具类 */
              .text-center { text-align: center; }
              .text-left { text-align: left; }
              .text-xs { font-size: 0.75rem; }
              .text-sm { font-size: 0.875rem; }
              .text-base { font-size: 1rem; }
              .text-lg { font-size: 1.125rem; }
              .text-xl { font-size: 1.25rem; }
              .text-2xl { font-size: 1.5rem; }
              .text-3xl { font-size: 1.875rem; }
              .text-4xl { font-size: 2.25rem; }
              .text-5xl { font-size: 3rem; }
              .text-6xl { font-size: 3.75rem; }
              .text-8xl { font-size: 6rem; }
              .text-9xl { font-size: 8rem; }
              .text-12xl { font-size: 8rem; }
              .font-bold { font-weight: 700; }
              .font-600 { font-weight: 600; }

              /* 颜色工具类 */
              .text-black { color: #000; }
              .text-white { color: #fff; }
              .text-gray-300 { color: #d1d5db; }
              .text-gray-500 { color: #6b7280; }
              .text-gray-600 { color: #4b5563; }
              .text-gray-700 { color: #374151; }
              .text-gray-800 { color: #1f2937; }
              .text-gray-950 { color: #030712; }
              .text-slate-600 { color: #475569; }
              .text-blue-700 { color: #1d4ed8; }
              .text-green-800 { color: #166534; }
              .bg-white { background-color: #fff; }
              .bg-gray-50 { background-color: #f9fafb; }
              .bg-gray-100 { background-color: #f3f4f6; }
              .bg-gray-300 { background-color: #d1d5db; }
              .bg-slate-50 { background-color: #f8fafc; }
              .bg-blue-500 { background-color: #3b82f6; }
              .bg-blue-700 { background-color: #1d4ed8; }
              .bg-zinc-100 { background-color: #f4f4f5; }

              /* 边框工具类 */
              .border { border: 1px solid; }
              .border-b { border-bottom: 1px solid; }
              .border-gray-200 { border-color: #e5e7eb; }
              .border-gray-300 { border-color: #d1d5db; }
              .border-gray-900 { border-color: #111827; }
              .border-green-600 { border-color: #16a34a; }
              .rounded { border-radius: 0.25rem; }
              .rounded-lg { border-radius: 0.5rem; }
              .rounded-4 { border-radius: 1rem; }
              .rounded-7 { border-radius: 1.75rem; }
              .rounded-8 { border-radius: 2rem; }

              /* 显示工具类 */
              .block { display: block; }
              .inline-block { display: inline-block; }
              .hidden { display: none; }
              .relative { position: relative; }
              .absolute { position: absolute; }

              /* 其他工具类 */
              .cursor-pointer { cursor: pointer; }
              .overflow-hidden { overflow: hidden; }

              /* 导航栏样式 */
              .nav {
                display: flex;
                flex-direction: row;
                justify-content: space-between;
                align-items: center;
                padding: 1rem 1.5rem;
                height: 3rem;
                width: 75rem;
                background-color: #fff;
                margin: auto;
              }

              .nav__logo-container {
                display: flex;
                flex-direction: row;
                align-items: center;
              }

              .nav__logo {
                height: 3rem;
              }

              .nav__separator {
                margin: 0 0.75rem;
                width: 0.125rem;
                height: 1.25rem;
                background-color: #E7E7E7;
              }

              .nav__social {
                display: flex;
                flex-direction: row;
                align-items: center;
                height: 2rem;
                width: 15rem;
              }

              .nav__social-link {
                display: inline-block;
                margin-right: 1.5rem;
                width: 2rem;
                height: 2rem;
              }

              .nav__menu {
                display: flex;
                flex-direction: row;
                align-items: center;
                text-align: center;
              }

              .nav__menu-link {
                font-size: 1rem;
                color: #363636;
                margin-right: 5rem;
              }

              /* 主内容区域 */
              .main-content {
                margin: auto;
                background-color: #fff;
                width: 50rem;
              }

              /* 文章卡片样式 */
              .article-card {
                margin-top: 5rem;
                margin-bottom: 4rem;
                padding: 1rem;
                border-radius: 0.5rem;
              }

              .article-card__title {
                width: 50rem;
                font-size: 3rem;
                font-weight: 700;
                text-align: center;
                color: #1E1E1E;
              }

              .article-card__meta {
                text-align: center;
                color: #9B9B9B;
                margin: 1rem 0;
              }

              .article-card__meta-item {
                font-size: 1rem;
              }

              .article-card__meta-separator {
                margin: 0 1rem;
              }

              .article-card__cover {
                display: flex;
                flex-direction: row;
                justify-content: center;
                align-items: center;
                padding: 0.75rem 4rem;
                margin: 2rem 0;
                background-size: cover;
                background-position: center;
                background-repeat: no-repeat;
                height: 20rem;
              }

              .article-card__summary {
                /* markdown-body class 会提供样式 */
              }

              .article-card__footer {
                display: flex;
                flex-direction: row;
                justify-content: center;
                align-items: center;
                margin-top: 4rem;
              }

              /* 按钮样式 */
              .btn {
                padding: 0.5rem 1.25rem;
                border-radius: 1.75rem;
                cursor: pointer;
                font-size: 1rem;
                text-align: center;
                display: inline-block;
              }

              .btn--read-more {
                background-color: #fff;
                width: 17.5rem;
                height: 3.75rem;
                border: 1px solid #d1d5db;
                color: #1f2937;
              }

              .btn--primary {
                background-color: #1d4ed8;
                color: #fff;
                border: 1px solid #1e40af;
              }

              /* 分隔线 */
              .divider {
                height: 0.0625rem;
                background-color: #d1d5db;
                margin: 1rem 0;
              }

              /* 后台管理样式 */
              .admin-header {
                border-bottom: 1px solid #e5e7eb;
                background-color: #fff;
              }

              .admin-nav {
                display: flex;
                flex-direction: row;
                align-items: center;
                justify-content: space-between;
                flex-wrap: wrap;
                padding: 1.5rem;
                max-width: 64rem;
                margin: auto;
              }

              .admin-nav__brand {
                display: flex;
                flex-direction: row;
                align-items: center;
                flex-shrink: 0;
                color: #000;
                margin-right: 1.5rem;
              }

              .admin-nav__title {
                font-size: 3rem;
                font-weight: 600;
              }

              .admin-nav__menu {
                display: flex;
                flex: 1 1 auto;
                flex-direction: row;
                align-items: center;
                justify-content: space-between;
              }

              .admin-nav__links {
                font-size: 0.875rem;
              }

              .admin-nav__link {
                margin-top: 1rem;
                font-size: 1.5rem;
                margin-right: 1rem;
                color: #000;
              }

              .admin-nav__link--active {
                color: #166534;
                border-bottom: 1px solid #16a34a;
              }

              .admin-nav__actions {
                display: flex;
                flex-direction: row;
              }

              .admin-nav__lang {
                position: relative;
                font-size: 0.875rem;
                width: 2rem;
                height: 1.5rem;
                margin-right: 0.75rem;
              }

              .admin-nav__github {
                font-size: 0.875rem;
                width: 1.5rem;
                height: 1.5rem;
              }

              .admin-nav__logout {
                display: flex;
                flex-direction: row;
                align-items: center;
                font-size: 0.875rem;
                height: 1.5rem;
                color: #475569;
                margin-left: 1rem;
              }

              .admin-nav__logout-icon {
                width: 1.25rem;
                height: 1.25rem;
              }

              /* 管理页面容器 */
              .admin-container {
                max-width: 64rem;
                padding: 1.5rem;
                background-color: #fff;
                border-radius: 0.5rem;
                margin: 2rem auto;
              }

              /* 表格样式 */
              .admin-table {
                width: 100%;
                text-align: left;
                color: #6b7280;
                border-collapse: collapse;
              }

              .admin-table__head {
                background-color: #f9fafb;
                color: #374151;
                font-size: 0.75rem;
              }

              .admin-table__cell {
                padding: 1.5rem 1rem;
              }

              .admin-table__row {
                background-color: #fff;
                border-bottom: 1px solid #111827;
              }

              /* 登录表单 */
              .login-container {
                display: flex;
                flex-direction: column;
                flex-grow: 1;
                justify-content: center;
                align-items: center;
                width: 100%;
                height: 100vh;
                background-color: #f8fafc;
              }

              .login-form {
                width: 25rem;
                padding: 1rem;
                border-radius: 2rem;
                background-color: #fff;
              }

              /* 统计卡片 */
              .stat-card {
                padding: 0.75rem;
                width: 18.75rem;
                margin: 1rem;
                background-color: #fff;
              }

              .stat-card__label {
                color: #d1d5db;
                font-size: 1rem;
                height: 1.5rem;
                display: flex;
                align-items: center;
                justify-content: flex-start;
              }

              .stat-card__value {
                color: #030712;
                font-size: 2rem;
                height: 2.5rem;
                display: flex;
                align-items: center;
                justify-content: flex-start;
              }

              /* 描述列表 */
              .desc-item {
                display: flex;
                flex-direction: row;
                width: 15rem;
                border: 1px solid #f3f4f6;
              }

              .desc-item__label {
                display: flex;
                align-items: center;
                font-weight: 700;
                font-size: 3rem;
                background-color: #f9fafb;
                width: 5rem;
                padding: 0.5rem;
                font-size: 1.5rem;
              }

              .desc-item__content {
                width: 10rem;
                padding: 0.5rem;
                display: flex;
                align-items: center;
                font-size: 1rem;
              }

              /* 同步日志 */
              .sync-log {
                padding: 0.5rem;
                border-radius: 2rem;
                background-color: #f9fafb;
              }

              .sync-log__output {
                padding: 1rem;
                border-radius: 0.5rem;
              }

              /* 表单样式 */
              .form-item {
                margin: 0 auto 1.25rem auto;
              }

              .form-item__label {
                display: block;
                margin-bottom: 0.5rem;
                font-size: 1.125rem;
                color: #111827;
              }

              .form-item__input {
                background-color: #f9fafb;
                border: 1px solid #111827;
                border-radius: 0.5rem;
                padding: 0.5rem;
                width: 25rem;
              }

              /* Alert 样式 */
              .alert {
                padding: 1rem 1.5rem;
                margin: 0.5rem;
                border-radius: 1rem;
                font-size: 1.125rem;
                display: flex;
                flex-direction: row;
                align-items: center;
              }

              .alert--info { background-color: #bfdbfe; color: #1e40af; }
              .alert--success { background-color: #bbf7d0; color: #166534; }
              .alert--warning { background-color: #fed7aa; color: #9a3412; }
              .alert--error { background-color: #fecaca; color: #991b1b; }

              .alert__icon {
                width: 1.25rem;
                height: 1.25rem;
                margin-right: 0.75rem;
              }

              /* Footer 样式 */
              .footer {
                padding: 1.25rem;
                background-color: #f7f8f9;
                display: flex;
                flex-direction: row;
                justify-content: center;
                align-items: center;
                color: #d1d5db;
              }

              .footer__content {
                display: flex;
                flex-direction: column;
                align-items: center;
              }

              .footer__logo {
                width: 12.25rem;
              }

              .footer__text {
                color: #e7e5e4;
              }

              .footer__text--highlight {
                color: #334155;
              }

              /* Dropdown 样式补充 */
              .dropdown-content {
                position: absolute;
                min-width: 10rem;
                background-color: #fff;
              }

              .dropdown-content a {
                display: flex;
                flex-direction: row;
                align-items: center;
                color: #4b5563;
                padding: 0.75rem 1rem;
              }

              /* Radio 样式 */
              .radio-group {
                margin: 0 auto 1.25rem auto;
              }

              .radio-group__label {
                display: block;
                margin-bottom: 0.5rem;
                font-size: 1.125rem;
                color: #111827;
              }

              .radio-group__options {
                display: flex;
                flex-direction: row;
              }

              .radio-option {
                margin-right: 1.5rem;
              }

              .radio-option__input {
                margin-right: 0.5rem;
              }

              /* 尺寸辅助类 */
              .w-4 { width: 1rem; }
              .w-8 { width: 2rem; }
              .w-20 { width: 5rem; }
              .w-40 { width: 10rem; }
              .w-49 { width: 12.25rem; }
              .h-4 { height: 1rem; }
              .size-5 { width: 1.25rem; height: 1.25rem; }
              .size-8 { width: 2rem; height: 2rem; }
            """)
          }
        }
        link {
          rel = "stylesheet"
          href = "/public/github-markdown-light.css"
        }
        headBlock()
      }

      body(block = bodyBlock)
    }
  }
}
