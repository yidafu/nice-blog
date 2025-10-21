package dev.yidafu.nicemaker.theme.simple.components

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
        classes = setOf("btn", "btn--primary", "py-2", "px-4", "rounded")
        type = item.type
        id = item.id
        name = item.id
        +item.value
      }

    else ->
      div {
        classes = setOf("form-item")
        label {
          classes = setOf("form-item__label")
          htmlFor = item.id
          +item.label
        }
        input {
          classes = setOf("form-item__input")
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
  div {
    classes = setOf("radio-group")
    label {
      classes = setOf("radio-group__label")
      +item.label
    }
    div {
      classes = setOf("radio-group__options")
      item.options.forEach { opt ->
        div {
          classes = setOf("radio-option")
          input(InputType.radio) {
            classes = setOf("radio-option__input")
            name = item.name
            value = opt.value
            id = opt.id
            checked = opt.value == item.value
          }
          label {
            htmlFor = opt.id
            +opt.label
          }
        }
      }
    }
  }
}
