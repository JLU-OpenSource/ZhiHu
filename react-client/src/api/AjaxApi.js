import moment from "moment";

const host = 'http://127.0.0.1:8080';

const AjaxApi = {

  body: function (body, args) {
    return {
      'args': args,
      'user': JSON.parse(sessionStorage.getItem('user')),
      'time': moment().format('YYYY-MM-DD HH:mm:ss'),
      'body': body
    }
  },

  post: function (url, data) {
    return fetch(host + url, {
      body: JSON.stringify(data),
      credentials: 'include',
      headers: {
        'token': sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      method: 'POST',
    }).then(response => response.json());
  }
}

export default AjaxApi;