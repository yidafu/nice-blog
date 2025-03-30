package dev.yidafu.blog.themes.simple.components

import io.github.allangomes.kotlinwind.css.*
import kotlinx.html.*

data class FormItem(
  val id: String,
  val label: String,
  val value: String,
  val type: InputType,
  val placeholder: String = "",
  val required: Boolean = false,
)

internal inline fun FlowContent.formItem(item: FormItem) {
  return when (item.type) {
    InputType.hidden ->
      input {
        type = item.type
        value = item.value
        id = item.id
        name = item.id
      }
    InputType.submit ->
      input {
        // bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 border border-blue-700 rounded
        style =
          kw.inline {
            background.blue[I500]
            text.white
            font.bold
            padding.y[2].x[4]
            border.blue[I700].rounded[4]
          }
        type = item.type
        id = item.id
        name = item.id
        +item.value
      }

    else ->
      div("m-auto") {
        style = kw.inline { margin.bottom[5] }
        label {
          style =
            kw.inline {
              block.margin.bottom[2]
              font.size[LG]
              text.gray[I900]
            }
          htmlFor = item.id
          +item.label
        }
        input {
          style =
            kw.inline {
              background.gray[I50]
              border[1].rounded[LG].gray[I900]
              padding[2]
              width[100]
            }
          type = item.type
          id = item.id
          value = item.value
          name = item.id
          placeholder = item.placeholder
          required = item.required
        }
      }
  }
}

internal data class RadioItem(
  val name: String,
  val label: String,
  val value: String,
  val options: List<Option>,
) {
  data class Option(val id: String, val label: String, val value: String)
}

internal inline fun FlowContent.radioItem(item: RadioItem) {
  fieldSet {
    legend {
      +item.label
    }
    item.options.forEach { option ->
      div("m-auto") {
        input {
          type = InputType.radio
          id = option.id
          value = option.value
          name = item.name
          checked = item.value == option.value
        }
        label {
          style =
            kw.inline {
              font.size[LG]
              text.gray[I900]
              margin.left[2]
            }
          htmlFor = option.id
          +option.label
        }
      }
    }
  }
}
