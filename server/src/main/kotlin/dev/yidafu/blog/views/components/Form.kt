package dev.yidafu.blog.views.components

import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.I900
import io.github.allangomes.kotlinwind.css.LG
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

internal data class FormItem(
  val id: String,
  val label: String,
  val value: String,
  val type: InputType,
  val placeholder: String = "",
)

internal inline fun FlowContent.formItem(item: FormItem) {
  div("m-auto") {
    style = kw.inline { margin.bottom[5] }
    label {
      style = kw.inline { block.margin.bottom[2];font.size[LG]; text.gray[I900] }
      htmlFor = item.id
      +item.label
    }
    input {
      style = kw.inline { background.gray[I50]; border[1].rounded[LG].gray[I900]; padding[2]; width[100] }
      type = item.type
      id = item.id
      value = item.value
      name = item.id
      placeholder = item.placeholder
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
          style = kw.inline {  font.size[LG]; text.gray[I900]; margin.left[2] }
          htmlFor = option.id
          +option.label
        }
      }
    }
  }

}
