import AjaxApi from './AjaxApi.js';

const QuestionApi = {
  allQuestion: function (page, callback) {
    AjaxApi.post('/api/question/all', AjaxApi.body({}, { page: page, size: '3' }))
      .then(response => {
        callback(response.status === 200 ? response.body : null);
      })
  }
}

export default QuestionApi;