import React from 'react';
import { Comment, List } from 'antd';
import moment from 'moment';
import 'moment/locale/zh-cn';
import AjaxApi from '../api/AjaxApi.js'

moment.locale('zh-cn');

class Comments extends React.Component {

  state = {
    data: []
  }

  async componentDidMount() {
    if (this.props.type === 'article') {
      fetch(AjaxApi.host + "/api/comment/article/" + this.props.id)
        .then(response => response.json())
        .then(response => this.setState({ data: response.body }))
    } else {
      fetch(AjaxApi.host + "/api/comment/answer/" + this.props.id)
        .then(response => response.json())
        .then(response => this.setState({ data: response.body }))
    }
  }

  render() {
    return (
      <div>
        <List
          pagination={{ pageSize: 5 }}
          className="comment-list"
          itemLayout="horizontal"
          dataSource={this.state.data}
          renderItem={item => (
            <Comment
              author={item.author.name}
              avatar={item.author.avatar}
              content={<p>{item.content}</p>}
              datetime={moment(item.st).format('YYYY-MM-DD HH:mm:ss')}
            />
          )}
        />
      </div>);
  }
}

export default Comments;