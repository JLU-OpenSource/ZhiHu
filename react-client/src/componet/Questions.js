import React from 'react';
import { List, Avatar, Icon, Empty, Pagination, Drawer } from 'antd';
import QuestionApi from '../api/QuestionApi';
import QuestionDetail from './QuestionDetail';
import AjaxApi from '../api/AjaxApi.js'

class Questions extends React.Component {

  state = {
    page: 1,
    data: [],
    drawerVisible: false,
    drawerTitle: '',
    count: 0
  }

  onDrawerClose = () => {
    this.setState({ drawerVisible: false })
  }

  pullQuestion = () => {
    const _this = this;
    QuestionApi.allQuestion((this.state.page - 1) + "", function (questions) {
      const data = [];
      if (questions !== null)
        for (let i = 0; i < questions.length; i++) {
          data.push(
            {
              key: questions[i].id,
              title: questions[i].title,
              author: {
                name: questions[i].author.name,
                avatar: questions[i].author.avatar,
                sign: questions[i].author.sign,
              },
              content: questions[i].summary,
            }
          );
        }
      _this.setState({ data: data })
    })
  }

  async componentDidMount() {
    this.pullQuestion();
    fetch(AjaxApi.host + "/api/question/count", {
      headers: {
        'token': sessionStorage.getItem('token'),
      }
    }).then(response => response.text())
      .then(text => this.setState({ count: parseInt(text) }))
  }

  onShowSizeChange = (currentPage) => {
    this.setState({ page: currentPage },
      () => this.pullQuestion())
  }

  render() {
    return (
      <div>
        <List
          itemLayout="vertical"
          size="large"
          locale={<Empty />}
          dataSource={this.state.data}
          footer={<Pagination simple pageSize={3} defaultCurrent={1} onChange={this.onShowSizeChange}
            total={this.state.count} style={{ marginTop: '10px', textAlign: 'center' }} />}
          renderItem={item => (
            <List.Item
              actions={
                [
                  <span onClick={() => this.props.allAnswerClick(item)}><Icon type="message" style={{ marginRight: 8 }} />查看全部答案</span>,
                  <span onClick={() => this.props.createAnswerClick(item)}><Icon type="edit" style={{ marginRight: 8 }} />撰写答案</span>
                ]}>
              <a href='javascrpit:void(0)' onClick={() =>
                this.setState({ drawerVisible: true, drawerTitle: item.title, key: item.key })}><h3>{item.title}</h3></a>
              <List.Item.Meta
                avatar={<Avatar src={item.author.avatar} />}
                title={<a href={item.href}>{item.author.name}</a>}
                description={item.author.sign}
              />
              {item.content}
            </List.Item>
          )}
        />
        <Drawer
          width={500}
          visible={this.state.drawerVisible}
          title={this.state.drawerTitle}
          placement="right"
          onClose={this.onDrawerClose}
          closable={true}>
          <QuestionDetail questionId={this.state.key} />
        </Drawer>
      </div>
    );
  }
}

export default Questions;