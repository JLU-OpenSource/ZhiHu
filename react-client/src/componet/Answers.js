import React from 'react';
import { List, Avatar, Icon, Empty, Pagination } from 'antd';
import AnswerApi from '../api/AnswerApi.js'
import AjaxApi from '../api/AjaxApi.js'

class Answers extends React.Component {

  state = {
    qid: this.props.qid,
    data: [],
    page: 0,
    count: 0
  }

  async componentDidMount() {
    this.pullAnswers(this.state.page)
    fetch(AjaxApi.host + "/api/answer/count", {
      headers: {
        'token': sessionStorage.getItem('token'),
      }
    }).then(response => response.text())
      .then(text => this.setState({ count: parseInt(text) }))
  }

  onShowSizeChange = (currentPage) => {
    this.setState({ page: currentPage },
      () => this.pullAnswers())
  }

  pullAnswers = (page) => {
    const data = [];
    const _this = this;
    AnswerApi.allUnderQuestion(this.state.qid, page, function (response) {
      if (response !== null)
        for (let i = 0; i < response.length; i++) {
          data.push(response[i]);
        }
      _this.setState({ data: data })
    });
  }

  render() {
    return (
      <List
        itemLayout="vertical"
        size="large"
        locale={<Empty />}
        dataSource={this.state.data}
        footer={<Pagination simple onChange={this.onShowSizeChange} pageSize={3} defaultCurrent={1} total={this.state.count} style={{ marginTop: '10px', textAlign: 'center' }} />}
        renderItem={item => (
          <List.Item
            actions={
              [
                <span><Icon type="star-o" style={{ marginRight: 8 }} />{item.collect.length} 收藏</span>,
                <span><Icon type="like-o" style={{ marginRight: 8 }} />{item.agree.length} 赞同</span>,
                <span><Icon type="message" style={{ marginRight: 8 }} />{item.comment.length} 评论</span>,
                <span onClick={() => this.props.fullAnswerClick(item)}><Icon type="read" style={{ marginRight: 8 }} />查看原答案</span>
              ]}>
            <a href='javascrpit:void(0)'><h3>{item.title}</h3></a>
            <List.Item.Meta
              avatar={<Avatar src={item.author.avatar} />}
              title={item.author.name}
              description={item.author.sign}
            />
            {item.summary}
          </List.Item>
        )}
      />
    );
  }
}

export default Answers;