package dev.yidafu.blog.admin.views.pages

import dev.yidafu.blog.common.FormKeys
import dev.yidafu.blog.common.Routes
import dev.yidafu.blog.common.view.components.EAlertType
import dev.yidafu.blog.common.view.components.alert
import dev.yidafu.blog.common.view.layouts.BaseLayout
import dev.yidafu.blog.common.view.tpl.PageTemplate
import dev.yidafu.blog.common.views.components.FormItem
import dev.yidafu.blog.common.views.components.formItem
import dev.yidafu.blog.common.vo.AdminLoginVO
import dev.yidafu.blog.i18n.AdminTxt
import io.github.allangomes.kotlinwind.css.I50
import io.github.allangomes.kotlinwind.css.I500
import io.github.allangomes.kotlinwind.css.I700
import io.github.allangomes.kotlinwind.css.kw
import kotlinx.html.*

class AdminLoginPage(override val vo: AdminLoginVO) : PageTemplate<AdminLoginVO>() {
  private val LOGON_FROM = "login_from"
  private val layout =
    BaseLayout(
      headScript = {
        script {
          type = "text/javascript"
          unsafe {
            +"""
              // https://github.com/mdn/dom-examples/blob/main/web-crypto/encrypt-decrypt/rsa-oaep.js
              // https://stackoverflow.com/questions/9267899/how-can-i-convert-an-arraybuffer-to-a-base64-encoded-string
              // https://github.com/mdn/dom-examples/blob/main/web-crypto/import-key/spki.js
              function str2ab(str) {
                const buf = new ArrayBuffer(str.length);
                const bufView = new Uint8Array(buf);
                for (let i = 0, strLen = str.length; i < strLen; i++) {
                  bufView[i] = str.charCodeAt(i);
                }
                return buf;
              }

              function arrayBufferToBase64(arrayBuffer) {
                const base64String = btoa(String.fromCharCode(...new Uint8Array(arrayBuffer)));
                return base64String;
              }
              function importRsaKey(pem) {
                // fetch the part of the PEM string between header and footer
                const pemHeader = "-----BEGIN PUBLIC KEY-----";
                const pemFooter = "-----END PUBLIC KEY-----";
                const pemContents = pem.substring(
                  pemHeader.length,
                  pem.length - pemFooter.length - 1,
                );
                // base64 decode the string to get the binary data
                const binaryDerString = window.atob(pemContents);
                // convert from a binary string to an ArrayBuffer
                const binaryDer = str2ab(binaryDerString);

                return window.crypto.subtle.importKey(
                  "spki",
                  binaryDer,
                  {
                    name: "RSA-OAEP",
                    hash: "SHA-256",
                  },
                  true,
                  ["encrypt"],
                );
              }
              document.addEventListener("DOMContentLoaded", (event) => {

                const submitDom = document.getElementById("submit");
                const publicKeyDom = document.getElementById("public_key");
                const formDom = document.getElementById("login_from");
                const passwordDom = document.getElementById("password");

                submitDom.onclick = async (e) => {
                  e.preventDefault();
                  const publicKey = publicKeyDom.value;
                  const encryptionKey = await importRsaKey(publicKey);
                  let enc = new TextEncoder();
                  const message = passwordDom.value;
                  const encoded = enc.encode(message);
                  const cipherText = await window.crypto.subtle.encrypt(
                    {
                      name: "RSA-OAEP"
                    },
                    encryptionKey,
                    encoded
                  );
                  const base64Text = arrayBufferToBase64(cipherText);
                  passwordDom.value = base64Text;
                  formDom.requestSubmit();
                }
              });
              """.trimIndent()
          }
        }
      },
    )

  private fun getOptions(vo: AdminLoginVO) =
    listOf(
      FormItem(
        FormKeys.USER_NAME,
        AdminTxt.username.toString(vo.locale),
        "",
        InputType.text,
        AdminTxt.username_placeholder.toString(vo.locale),
        true,
      ),
      FormItem(
        FormKeys.PASSWORD,
        AdminTxt.password.toString(vo.locale),
        "",
        InputType.password,
        AdminTxt.password_placeholder.toString(vo.locale),
        true,
      ),
    )

  override fun render(): String {
    return layout.layout {
      div {
        style =
          kw.inline {
            flex.col.grow.justify_center.items_center
            width["100%"].height["100%"]
            background.slate[I50]
          }
        div("shadow-lg") {
          style =
            kw.inline {
              width[100].padding[4]
              border.rounded[8]
              background.white
            }
          // 避免提交表单时，被提交
          formItem(
            FormItem(FormKeys.PUBLIC_KEY, "", vo.publicKey, InputType.hidden),
          )

          form {
            id = LOGON_FROM
            action = Routes.LOGIN_URL
            method = FormMethod.post

            getOptions(vo).forEach { opt ->
              formItem(opt)
            }
            if (vo.errorMessage != null) {
              alert(vo.errorMessage!!, EAlertType.ERROR)
            }
            button {
              style =
                kw.inline {
                  background.blue[I500]
                  text.white
                  font.bold
                  padding.y[2].x[4]
                  border.blue[I700].rounded[4]
                }
              id = FormKeys.SUBMIT
              +AdminTxt.submit.toString(vo.locale)
            }
          }
        }
      }
    }.finalize()
  }
}
