import AjaxApi from "./AjaxApi";

const AnswerApi = {
  all: function (page, callback) {
    AjaxApi.post('/api/answer/all', AjaxApi.body({}, { page: page, size: 3 }))
      .then(response => {
        callback(response.status === 200 ? response.body : null);
      })
  },
  allUnderQuestion: function (qid, page, callback) {
    AjaxApi.post('/api/answer/question', AjaxApi.body(qid, { page: page, size: 3 }))
      .then(response => {
        callback(response.status === 200 ? response.body : null);
      })
  },
  metaData: function (answer, callback) {
    AjaxApi.post('/api/answer/metadata', AjaxApi.body(answer, {}))
      .then(response => {
        callback(response.status === 200 ? response.body : null);
      })
  }
}

export default AnswerApi;