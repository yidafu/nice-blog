package dev.yidafu.blog.themes.simple.pages.admin

// https://github.com/mdn/dom-examples/blob/main/web-crypto/encrypt-decrypt/rsa-oaep.js
// https://stackoverflow.com/questions/9267899/how-can-i-convert-an-arraybuffer-to-a-base64-encoded-string
// https://github.com/mdn/dom-examples/blob/main/web-crypto/import-key/spki.js
const val JS_LOGIN_CODE = """
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
"""
