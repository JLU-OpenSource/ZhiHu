import React from 'react';
import {
  List, Avatar, Icon, Empty, Typography,
  Divider, Input, Comment, Form, message, Drawer
} from 'antd';
import Comments from './Comments.js';
import AjaxApi from '../api/AjaxApi.js'
import 'braft-editor/dist/output.css'
import AnswerApi from '../api/AnswerApi.js';

class Answer extends React.Component {

  state = {
    comment: '',
    showComments: false,
    answer: this.props.answer,
    data: [this.props.answer],
    collectAcitve: false,
    collectCount: 0,
    agreeAcitve: false,
    agreeCount: 0,
    content: ''
  }
  async componentDidMount() {

    this.setState({ agreeCount: this.state.answer.agree.length, collectCount: this.state.answer.collect.length })

    fetch(AjaxApi.host + "/answer/" + this.state.answer.aid + ".html")
      .then(response => response.text())
      .then(text => this.setState({ content: text }))

    const _answer = this.state.answer;
    const user = JSON.parse(sessionStorage.getItem('user'))
    let collect = -1;
    for (let i = 0; i < _answer.collect.length; i++) {
      if (_answer.collect[i].id === user.id)
        collect = i;
    }
    let agree = -1;
    for (let i = 0; i < _answer.agree.length; i++) {
      if (_answer.agree[i].id === user.id)
        agree = i;
    }

    this.setState({
      collectAcitve: collect !== -1,
      agreeAcitve: agree !== -1
    })
  }

  handleSubmit = (e) => {
    e.preventDefault();
    if (this.state.comment.length === 0 ||
      this.state.comment.trim().length === 0) {
      message.error('不能发布空评论');
    } else {
      const comment = {
        content: this.state.comment,
        author: JSON.parse(sessionStorage.getItem('user'))
      }
      AjaxApi.post('/api/comment/create', AjaxApi.body(comment, { id: this.state.answer.aid, type: 'answer' })
      ).then(response => {
        if (response.status === 200) {
          message.success("成功发表评论")
          const answer = this.state.answer;
          answer.comment.push(response.body);
          this.setState({ answer: answer })
          this.setState({ comment: '' })
        } else
          message.error("发表评论失败")
      })
    }
  }

  onDrawerClose = (e) => {
    this.setState({ showComments: false });
  }

  handleInputChange = (e) => {
    this.setState({ comment: e.target.value });
  }

  handleMetaDataClick = (type) => {
    const user = JSON.parse(sessionStorage.getItem('user'))
    const _answer = this.state.answer;
    let index = -1;
    if (type) {
      for (let i = 0; i < _answer.agree.length; i++) {
        if (_answer.agree[i].id === user.id)
          index = i;
      }
      if (index === -1) {
        _answer.agree.push(user)
      } else {
        _answer.agree.splice(index, 1);
      }
      this.setState({
        answer: _answer,
        agreeAcitve: _answer.agree.indexOf(user) !== -1,
      },
        () => {
          if (this.state.agreeAcitve) {
            message.success("已赞同");
          } else {
            message.success("已取消赞同");
          }
        })
    } else {
      for (let i = 0; i < _answer.collect.length; i++) {
        if (_answer.collect[i].id === user.id)
          index = i;
      }
      if (index === -1) {
        _answer.collect.push(user)
      } else {
        _answer.collect.splice(index, 1);
      }
      this.setState({
        answer: _answer,
        collectAcitve: _answer.collect.indexOf(user) !== -1,
      },
        () => {
          if (this.state.collectAcitve) {
            message.success("已收藏");
          } else {
            message.success("已取消收藏");
          }
        })
    }
    AnswerApi.metaData(_answer, function (response) {
      console.log(response)
    })
  }

  render() {
    return (
      <div>
        <List
          itemLayout="vertical"
          size="large"
          locale={<Empty />}
          dataSource={this.state.data}
          renderItem={item => (
            <List.Item
              actions={
                [
                  <span className={this.state.collectAcitve ? "active" : null}
                    onClick={() => this.handleMetaDataClick(false)}
                  ><Icon type="star-o" style={{ marginRight: 8 }} />{this.state.answer.collect.length} 收藏</span>,
                  <span className={this.state.agreeAcitve ? "active" : null}
                    onClick={() => this.handleMetaDataClick(true)}
                  ><Icon type="like-o" style={{ marginRight: 8 }} />{this.state.answer.agree.length} 赞同</span>,
                  <span onClick={() => this.setState({ showComments: true })}><Icon type="message" style={{ marginRight: 8 }} />{this.state.answer.comment.length} 评论</span>,
                  <span onClick={() => this.props.createAnswerClick({ key: item.qid, title: item.title })}><Icon type="edit" style={{ marginRight: 8 }} />撰写答案</span>
                ]}>
              <a href='javascrpit:void(0)'><h3>{item.title}</h3></a>
              <List.Item.Meta
                avatar={<Avatar src={item.author.avatar} />}
                title={item.author.name}
                description={item.author.sign}
              />
              <Divider />
              <Typography>
                <div className="braft-output-content" dangerouslySetInnerHTML={{ __html: this.state.content }}></div>
              </Typography>
              <Divider />
            </List.Item>
          )
          }
        />
        <Form onSubmit={this.handleSubmit}>
          <Comment
            avatar={(
              <Avatar
                src={JSON.parse(sessionStorage.getItem('user')).avatar}
                alt="Han Solo"
              />
            )}
            content={(
              <Input placeholder='输入评论内容，然后按下回车发布' value={this.state.comment}
                onChange={(e) => { this.handleInputChange(e) }} />
            )}
          />
        </Form>
        <Drawer
          width={500}
          visible={this.state.showComments}
          title='评论'
          placement="right"
          onClose={this.onDrawerClose}
          closable={true}>
          <Comments id={this.state.answer.aid} type='answer' />
        </Drawer>
      </div>
    );
  }
}

export default Answer;