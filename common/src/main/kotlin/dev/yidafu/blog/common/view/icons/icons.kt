package dev.yidafu.blog.common.view.icons

import kotlinx.html.*
import com.github.nwillc.ksvg.elements.SVG

val languageIcon = SVG.svg(true) {
  viewBox = "0 0 1280 1024"
  cssClass = "icon"
  path {
    d =
      "M0 256c0-70.6 57.4-128 128-128h1024c70.6 0 128 57.4 128 128v512c0 70.6-57.4 128-128 128H128c-70.6 0-128-57.4-128-128V256z m640 0v512h512V256H640z m-283.4 95.8c-6.4-14.4-20.8-23.8-36.6-23.8s-30.2 9.4-36.6 23.8l-128 288c-9 20.2 0.2 43.8 20.4 52.8s43.8-0.2 52.8-20.4l17.8-40.2h147.2l17.8 40.2c9 20.2 32.6 29.2 52.8 20.4s29.2-32.6 20.4-52.8l-128-288zM320 466.4l38 85.6h-76l38-85.6zM896 328c22 0 40 18 40 40v8h120c22 0 40 18 40 40s-18 40-40 40h-4l-3.2 9c-17.8 48.8-44.8 93.2-79.2 130.8 1.8 1.2 3.6 2.2 5.4 3.2l37.8 22.6c19 11.4 25 36 13.8 54.8s-36 25-54.8 13.8L934 667.6c-9-5.4-17.6-11-26.2-17-21.2 15-43.8 28-68 38.8l-7.2 3.2c-20.2 9-43.8-0.2-52.8-20.4s0.2-43.8 20.4-52.8l7.2-3.2c12.8-5.8 25.2-12.2 37-19.6L820 572.2c-15.6-15.6-15.6-41 0-56.6s41-15.6 56.6 0l29.2 29.2 1 1c24.8-26.2 45-56.6 59.6-90H752c-22 0-40-18-40-40s18-40 40-40h104v-8c0-22 18-40 40-40z"
    fill = "currentColor"

  }
}

fun FlowContent.Language() {
  consumer.onTagContentUnsafe {
    raw(languageIcon.toString())
  }
}


val githubIcon = SVG.svg(true) {
  viewBox = "0 0 1024 1024"
  cssClass = "icon"
  path {
    d =
      "M511.6 76.3C264.3 76.2 64 276.4 64 523.5 64 718.9 189.3 885 363.8 946c23.5 5.9 19.9-10.8 19.9-22.2v-77.5c-135.7 15.9-141.2-73.9-150.3-88.9C215 726 171.5 718 184.5 703c30.9-15.9 62.4 4 98.9 57.9 26.4 39.1 77.9 32.5 104 26 5.7-23.5 17.9-44.5 34.7-60.8-140.6-25.2-199.2-111-199.2-213 0-49.5 16.3-95 48.3-131.7-20.4-60.5 1.9-112.3 4.9-120 58.1-5.2 118.5 41.6 123.2 45.3 33-8.9 70.7-13.6 112.9-13.6 42.4 0 80.2 4.9 113.5 13.9 11.3-8.6 67.3-48.8 121.3-43.9 2.9 7.7 24.7 58.3 5.5 118 32.4 36.8 48.9 82.7 48.9 132.3 0 102.2-59 188.1-200 212.9 23.5 23.2 38.1 55.4 38.1 91v112.5c0.8 9 0 17.9 15 17.9 177.1-59.7 304.6-227 304.6-424.1 0-247.2-200.4-447.3-447.5-447.3z"
    fill = "currentColor"

  }
}

fun FlowContent.Github() {
  consumer.onTagContentUnsafe {
    raw(githubIcon.toString())
  }
}

val englishIcon = SVG.svg {
  viewBox = "0 0 1024 1024"
  cssClass = "icon"
  path {
    d =
      "M798.992 96c70.4 0 127.632 56.4 128.984 126.472l0.024 2.536v573.984c0 70.4-56.4 127.632-126.472 128.984l-2.536 0.024H225.008c-70.4 0-127.632-56.4-128.984-126.472L96 798.992V225.008c0-70.4 56.4-127.632 126.472-128.984l2.536-0.024h573.984z m0 64H225.008a65.008 65.008 0 0 0-64.976 63.024l-0.032 1.984v573.984a65.008 65.008 0 0 0 63.024 64.976l1.984 0.032h573.984a65.008 65.008 0 0 0 64.976-63.024l0.032-1.984V225.008a65.008 65.008 0 0 0-63.024-64.976L798.992 160zM496.32 336v50H298.24v99h186.144v50H298.24v108h206.552v50H240V336h256.32z m191.12 91.504c63.264 0 95.6 33.976 96.536 102.88l0.024 3.12v159.496h-56.736V538.496c0-42.496-19.416-63.496-58.24-63.496-13.928 0-26.376 5-36.824 15-11.352 10.448-18.208 25.416-20.568 44.456l-0.336 3.04v155.504h-56.744V434.496h56.744v30c9.952-12 21.4-21.496 33.84-27.496 12.944-6.504 26.88-9.504 42.304-9.504z"
    fill = "currentColor"
  }
}

fun FlowContent.English() {
  consumer.onTagContentUnsafe {
    raw(englishIcon.toString())
  }
}


val chineseIcon = SVG.svg(true) {
  viewBox = "0 0 1024 1024"
  cssClass = "icon"
  path {
    d =
      "M555.231787 330.203429v-107.997284h-68.202727v108.038827H263.433935v273.457531H487.02906v210.976899h68.202727V603.70431h224.21827V330.203429H555.231787z m-68.202727 209.074952h-157.337694v-144.605675h157.335888v144.605675z m226.131053 0H555.195662v-144.605675h157.962645v144.605675z"
    fill = "#231815"
  }
  path {
    d =
      "M0.000903 921.600181V102.398013C0.000903 45.843575 45.846285 0 102.398916 0h819.200362c56.545407 0 102.398013 45.843575 102.398013 102.398013v819.202168c0 56.554438-45.854413 102.398013-102.398013 102.398013H102.398916C45.846285 1024 0.000903 978.154618 0.000903 921.600181z m947.194265 0V102.398013c0-14.139061-11.465861-25.595891-25.59589-25.595891H102.398916c-14.139061 0-25.594085 11.45683-25.594084 25.595891v819.202168c0 14.137255 11.455024 25.595891 25.594084 25.59589h819.200362c14.131836 0 25.595891-11.458636 25.59589-25.59589z"
    fill = "#231815"
  }
}

fun FlowContent.Chinese() {
  consumer.onTagContentUnsafe {
    raw(chineseIcon.toString())
  }
}


class AppearanceIcon : Icon {
  override fun render(container: FlowContent) {
    val icon = SVG.svg {
      cssClass = "icon"
      viewBox = "0 0 1024 1024"
      path {
        d =
          "M668.842667 896H358.528c-53.973333 0-97.792-46.250667-97.792-103.125333v-266.666667h-13.525333c-16.853333 0-33.706667-7.125333-47.189334-21.333333L105.557333 405.333333a75.776 75.776 0 0 1 0-103.125333l145.066667-152.874667c13.482667-14.208 30.336-21.333333 47.189333-21.333333h50.602667c20.224 0 37.12 10.666667 47.232 28.458667C419.242667 202.666667 463.061333 234.666667 513.706667 234.666667c50.56 0 94.421333-28.458667 118.016-78.208 10.154667-17.792 27.008-28.458667 43.861333-28.458667h50.602667c16.853333 0 37.12 7.125333 47.232 21.333333l145.024 152.874667c26.965333 28.458667 26.965333 74.666667 0 103.125333l-94.464 99.541334c-13.482667 14.208-30.336 21.333333-47.232 21.333333h-10.112v266.666667c-3.370667 53.333333-43.818667 99.584-97.792 103.125333zM284.330667 451.541333c3.413333 0 6.741333 0 10.112 3.541334a26.709333 26.709333 0 0 1 16.853333 24.917333v312.874667c0 28.458667 20.266667 49.792 47.232 49.792h310.314667c26.965333 0 47.232-21.333333 47.232-49.792V480c0-10.666667 6.741333-21.333333 16.853333-24.917333 10.112-3.541333 20.224-3.541333 26.965333 7.125333l6.741334 7.125333c3.413333 3.541333 6.784 7.082667 13.525333 7.082667 3.370667 0 10.112-3.541333 13.482667-7.082667l94.421333-99.541333c6.741333-7.125333 6.741333-17.792 0-28.458667l-145.024-152.874666c-3.370667-3.584-6.741333-7.125333-13.482667-7.125334h-50.602666c-30.336 67.541333-94.421333 106.666667-165.248 106.666667-70.826667 0-131.541333-42.666667-165.290667-106.666667H297.813333c-3.370667 0-10.112 3.541333-13.482666 7.125334L139.264 341.333333c-6.698667 7.125333-6.698667 17.792 0 28.458667L233.770667 469.333333c3.413333 3.541333 6.741333 7.082667 13.482666 7.082667 3.413333 0 10.154667-3.541333 13.525334-7.082667l6.741333-7.125333a18.432 18.432 0 0 1 16.853333-10.666667z"
        fill = "currentColor"
      }
    }
    container.consumer.onTagContentUnsafe {
      raw(icon.toString())
    }
  }
}


fun FlowContent.Sync() {
  SyncIcon().render(this).toString()
}

class SyncIcon : Icon {
  override fun render(container: FlowContent) {
    val icon = SVG.svg {
      cssClass = "icon"
      viewBox = "0 0 1024 1024"
      path {
        d =
          "M296 256c-4.4 0-8 3.6-8 8v48c0 4.4 3.6 8 8 8h384c4.4 0 8-3.6 8-8v-48c0-4.4-3.6-8-8-8H296zM488 456v-48c0-4.4-3.6-8-8-8H296c-4.4 0-8 3.6-8 8v48c0 4.4 3.6 8 8 8h184c4.4 0 8-3.6 8-8z"
        fill = "currentColor"
      }
      path {
        d =
          "M440 852H208V148h560v344c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V108c0-17.7-14.3-32-32-32H168c-17.7 0-32 14.3-32 32v784c0 17.7 14.3 32 32 32h272c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8z"
        fill = "currentColor"
      }
      path {
        d =
          "M544.1 736.4c1.8-34.5 16.2-66.8 40.8-91.4 26.2-26.2 62-41 99.1-41 37.4 0 72.6 14.6 99.1 41 3.2 3.2 6.3 6.6 9.2 10.1L769.2 673c-5.3 4.1-3.5 12.5 3 14.1l93.3 22.5c5 1.2 9.8-2.6 9.9-7.7l0.6-95.4c0-6.7-7.6-10.5-12.9-6.4l-20.3 15.8C805.4 569.6 748.1 540 684 540c-109.9 0-199.6 86.9-204 195.7-0.2 4.5 3.5 8.3 8 8.3h48.1c4.3 0 7.8-3.3 8-7.6zM880 744h-48.1c-4.3 0-7.8 3.3-8 7.6-1.8 34.5-16.2 66.8-40.8 91.4-26.2 26.2-62 41-99.1 41-37.4 0-72.6-14.6-99.1-41-3.2-3.2-6.3-6.6-9.2-10.1l23.1-17.9c5.3-4.1 3.5-12.5-3-14.1l-93.3-22.5c-5-1.2-9.8 2.6-9.9 7.7l-0.6 95.4c0 6.7 7.6 10.5 12.9 6.4l20.3-15.8C562.6 918.4 619.9 948 684 948c109.9 0 199.6-86.9 204-195.7 0.2-4.5-3.5-8.3-8-8.3z"
        fill = "currentColor"
      }
    }
    container.consumer.onTagContentUnsafe {
      raw(icon.toString())
    }

  }
}


class SourceIcon : Icon {
  override fun render(container: FlowContent) {
    val icon = SVG.svg(true) {
      cssClass = "icon"
      viewBox = "0 0 1024 1024"
      path {
        d =
          "M400 622.272c-118.912-7.264-220.544-37.504-280-79.712v81.408c0 55.488 121.056 101.568 280 110.4v55.904c-118.912-7.264-220.544-37.44-280-79.744v81.44H64V260C64 151.744 239.488 64 456 64c216.448 0 391.936 87.744 391.936 196v176.832h-56v-75.84c-68.576 56.896-193.408 94.976-335.968 94.976-142.56 0-267.36-38.08-336-94.976v94.976c0 55.488 121.088 101.6 280 110.432v55.872z m56 337.664c-216.512 0-392-75.2-392-168h56c0 61.888 150.4 112 336 112v56z m0-559.968c185.536 0 335.968-62.72 335.968-139.968 0-77.28-150.4-140-336-140-185.568 0-336 62.72-336 140 0 77.248 150.432 139.968 336 139.968z m487.264 367.36l-38.976-22.72c2.176-11.904 3.584-24.064 3.584-36.64a202.72 202.72 0 0 0-3.584-36.672l38.976-22.72c15.968-9.408 21.408-29.888 12.192-45.952l-33.28-58.176a33.152 33.152 0 0 0-45.568-12.32l-39.424 22.976a199.36 199.36 0 0 0-62.592-36.96v-28.576c0-18.528-14.88-33.6-33.312-33.6h-66.624a33.472 33.472 0 0 0-33.344 33.6v28.576a199.36 199.36 0 0 0-62.592 36.96l-39.424-22.976a33.152 33.152 0 0 0-45.536 12.32l-33.28 58.24a33.76 33.76 0 0 0 12.16 45.92l39.008 22.688a201.179 201.179 0 0 0-3.584 36.672c0 12.576 1.376 24.736 3.584 36.672l-38.976 22.72c-16 9.376-21.44 29.856-12.224 45.92l33.312 58.176a33.152 33.152 0 0 0 45.536 12.32l39.424-22.944a198.784 198.784 0 0 0 62.592 36.96v28.544c0 18.56 14.912 33.6 33.344 33.6h66.624a33.472 33.472 0 0 0 33.312-33.6v-28.544a199.36 199.36 0 0 0 62.592-36.96l39.424 22.944a33.216 33.216 0 0 0 45.536-12.32l33.312-58.24a33.76 33.76 0 0 0-12.192-45.888z m-41.376 33.088l-15.36 26.144a15.52 15.52 0 0 1-20.992 5.472l-42.656-24.16a153.984 153.984 0 0 1-84.16 47.808v33.216c0 8.288-6.912 15.04-15.424 15.04h-30.688a15.232 15.232 0 0 1-15.36-15.104V855.68a153.984 153.984 0 0 1-84.192-47.808L550.4 832a15.52 15.52 0 0 1-21.024-5.472l-15.36-26.144a14.88 14.88 0 0 1 5.632-20.544l42.848-24.32a147.2 147.2 0 0 1-8.128-47.584c0-16.704 3.008-32.608 8.16-47.616L519.648 636a14.88 14.88 0 0 1-5.6-20.512l15.36-26.176a15.52 15.52 0 0 1 20.992-5.472l42.656 24.192a153.984 153.984 0 0 1 84.16-47.808V527.04c0-8.32 6.88-15.04 15.392-15.04h30.72c8.48 0 15.36 6.72 15.36 15.104v33.152a153.984 153.984 0 0 1 84.16 47.808l42.688-24.192a15.52 15.52 0 0 1 20.992 5.472l15.36 26.176a14.88 14.88 0 0 1-5.6 20.544l-42.848 24.32c5.024 14.976 8.128 30.88 8.128 47.584 0 16.672-3.04 32.576-8.128 47.584l42.848 24.32a14.88 14.88 0 0 1 5.6 20.544z m-193.92-176.448a84 84 0 1 0 0 168 84 84 0 0 0 0-168z m0 112a28 28 0 1 1 0-56 28 28 0 0 1 0 56z"
        fill = "currentColor"
      }
    }
    container.consumer.onTagContentUnsafe {
      raw(icon.toString())
    }
  }
}


val alertSuccessIcon = SVG.svg(true) {
  viewBox = "0 0 24 24"
  cssClass = "icon"
  path {
    fill = "currentColor"
    d =
      "M12,0A12,12,0,1,0,24,12,12.014,12.014,0,0,0,12,0Zm6.927,8.2-6.845,9.289a1.011,1.011,0,0,1-1.43.188L5.764,13.769a1,1,0,1,1,1.25-1.562l4.076,3.261,6.227-8.451A1,1,0,1,1,18.927,8.2Z"
  }
}

val alertWarningIcon = SVG.svg(true) {
  viewBox = "0 0 24 24"
  cssClass = "icon"
  path {
    fill = "currentColor"
    d =
      "M23.119,20,13.772,2.15h0a2,2,0,0,0-3.543,0L.881,20a2,2,0,0,0,1.772,2.928H21.347A2,2,0,0,0,23.119,20ZM11,8.423a1,1,0,0,1,2,0v6a1,1,0,1,1-2,0Zm1.05,11.51h-.028a1.528,1.528,0,0,1-1.522-1.47,1.476,1.476,0,0,1,1.448-1.53h.028A1.527,1.527,0,0,1,13.5,18.4,1.475,1.475,0,0,1,12.05,19.933Z"
  }
}


val alertInfoIcon = SVG.svg {
  viewBox = "0 0 24 24"
  cssClass = "icon"
  path {
    fill = "currentColor"
    d =
      "M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm.25,5a1.5,1.5,0,1,1-1.5,1.5A1.5,1.5,0,0,1,12.25,5ZM14.5,18.5h-4a1,1,0,0,1,0-2h.75a.25.25,0,0,0,.25-.25v-4.5a.25.25,0,0,0-.25-.25H10.5a1,1,0,0,1,0-2h1a2,2,0,0,1,2,2v4.75a.25.25,0,0,0,.25.25h.75a1,1,0,1,1,0,2Z"
  }
}


val alertErrorIcon = SVG.svg(true) {
  viewBox = "0 0 24 24"
  cssClass = "icon"
  path {
    fill = "currentColor"
    d =
      "M11.983,0a12.206,12.206,0,0,0-8.51,3.653A11.8,11.8,0,0,0,0,12.207,11.779,11.779,0,0,0,11.8,24h.214A12.111,12.111,0,0,0,24,11.791h0A11.766,11.766,0,0,0,11.983,0ZM10.5,16.542a1.476,1.476,0,0,1,1.449-1.53h.027a1.527,1.527,0,0,1,1.523,1.47,1.475,1.475,0,0,1-1.449,1.53h-.027A1.529,1.529,0,0,1,10.5,16.542ZM11,12.5v-6a1,1,0,0,1,2,0v6a1,1,0,1,1-2,0Z"
  }
}


val logoutIcon = SVG.svg(true) {
  cssClass = "icon"
  viewBox = "0 0 1024 1024"
  path {
    d =
      "M896 512c0-14.16-6.128-26.896-15.888-35.68l-160-144a48 48 0 0 0-64.224 71.36L722.912 464H432a48 48 0 1 0 0 96h290.912l-67.008 60.32a48 48 0 1 0 64.208 71.36l160-144A47.84 47.84 0 0 0 896 512zM416 832H224V192h192a48 48 0 1 0 0-96H176a48 48 0 0 0-48 48v736a48 48 0 0 0 48 48h240a48 48 0 1 0 0-96z"
    fill = "currentColor"
  }
}

fun FlowContent.Logout() {
  consumer.onTagContentUnsafe {
    raw(logoutIcon.toString())
  }
}


val syncLogIcon = SVG.svg {
  cssClass = "icon"

  viewBox = "0 0 1056 1024"
  path {
    d =
      "M447.856564 1023.945067H93.84388A90.792046 90.792046 0 0 1 0 936.967813V87.032187A90.792046 90.792046 0 0 1 93.84388 0.054933h707.26241a90.792046 90.792046 0 0 1 93.843879 86.977254v331.886892a39.673835 39.673835 0 1 1-76.295837 0V87.032187a15.259167 15.259167 0 0 0-14.496209-6.866625H93.84388a14.496209 14.496209 0 0 0-13.733251 6.866625v849.935626s4.57775 6.866625 13.733251 6.866625h354.012684a39.673835 39.673835 0 0 1 39.673835 39.673835 40.436794 40.436794 0 0 1-39.673835 40.436794z"
    fill = "currentColor"
  }
  path {
    d =
      "M486.004482 273.956988h-289.924181a40.436794 40.436794 0 0 1 0-76.295837h289.924181a40.436794 40.436794 0 1 1 0 76.295837zM711.077202 457.829956H196.080301a40.436794 40.436794 0 0 1 0-76.295837h514.233942a40.436794 40.436794 0 1 1 0 76.295837z m289.924181 338.753516A262.45768 262.45768 0 0 0 605.788947 534.125793a39.673835 39.673835 0 1 0 41.199752 68.666253 184.635926 184.635926 0 0 1 224.309761 27.466502 181.584092 181.584092 0 0 1 51.881169 139.621381l-51.118211-16.785084 61.03667 122.836298 123.599256-61.799628-55.695961-18.311001zM834.676458 915.604978A183.872967 183.872967 0 0 1 610.366697 890.427352a173.954509 173.954509 0 0 1-51.881169-139.621382l51.11821 17.548043-61.036669-122.836298-122.836298 60.273711 54.170044 18.311001a260.931763 260.931763 0 0 0 259.405847 299.079682 257.879929 257.879929 0 0 0 135.80659-38.147919 39.673835 39.673835 0 1 0-40.436794-67.903295z"
    fill = "currentColor"
  }
}


fun FlowContent.SyncLog() {
  consumer.onTagContentUnsafe {
    raw(syncLogIcon.toString())
  }
}

class SyncLogIcon: Icon {
  override fun render(container: FlowContent) {
    container.SyncLog()
  }

}

val emailIcon = SVG.svg {
    viewBox = "0 0 1024 1024"
    path {
      d =
        "M874.666667 181.333333H149.333333c-40.533333 0-74.666667 34.133333-74.666666 74.666667v512c0 40.533333 34.133333 74.666667 74.666666 74.666667h725.333334c40.533333 0 74.666667-34.133333 74.666666-74.666667V256c0-40.533333-34.133333-74.666667-74.666666-74.666667z m-725.333334 64h725.333334c6.4 0 10.666667 4.266667 10.666666 10.666667v25.6L512 516.266667l-373.333333-234.666667V256c0-6.4 4.266667-10.666667 10.666666-10.666667z m725.333334 533.333334H149.333333c-6.4 0-10.666667-4.266667-10.666666-10.666667V356.266667l356.266666 224c4.266667 4.266667 10.666667 4.266667 17.066667 4.266666s12.8-2.133333 17.066667-4.266666l356.266666-224V768c0 6.4-4.266667 10.666667-10.666666 10.666667z"
      fill = "currentColor"
    }
}

fun FlowContent.Email() {
  consumer.onTagContentUnsafe {
    raw(emailIcon.toString())
  }
}
