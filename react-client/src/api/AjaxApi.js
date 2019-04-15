import moment from "moment";

const AjaxApi = {

  host: "http://127.0.0.1:8080",

  body: function (body, args) {
    return {
      'args': args,
      'user': JSON.parse(sessionStorage.getItem('user')),
      'time': moment().format('YYYY-MM-DD HH:mm:ss'),
      'body': body
    }
  },

  post: function (url, data) {
    return fetch(this.host + url, {
      body: JSON.stringify(data),
      headers: {
        'token': sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      method: 'POST',
    }).then(response => response.json());
  }
}

export default AjaxApi;