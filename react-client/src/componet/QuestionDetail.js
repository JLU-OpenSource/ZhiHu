import React from 'react';
import AjaxApi from '../api/AjaxApi';
import { List, Avatar, Typography, Divider } from 'antd'
import 'braft-editor/dist/output.css'

class QuestionDetail extends React.Component {

  state = {
    question: {
      title: '',
      author: {
      },
      id: 0
    },
    content: ''
  }

  getContent = () => {
    fetch(AjaxApi.host + "/question/" + this.state.question.id + ".html")
      .then(response => response.text())
      .then(text => this.setState({ content: text }))
  }

  async componentDidMount() {
    fetch(AjaxApi.host + "/api/question/" + this.props.questionId)
      .then(response => response.json())
      .then(json => this.setState({ question: json.body }, () => this.getContent()));
  }
  render() {
    return (
      <div>
        <List.Item.Meta
          avatar={<Avatar src={this.state.question.author.avatar} />}
          title={this.state.question.author.name}
          description={this.state.question.author.sign}
        />
        <Typography>
          <Divider />
          <div className="braft-output-content" dangerouslySetInnerHTML={{ __html: this.state.content }}></div>
        </Typography>
      </div>
    );
  }
}

export default QuestionDetail;