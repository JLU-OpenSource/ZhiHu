import React from 'react';
import {
  List, Avatar, Icon, Empty, Typography,
  Divider, Input, Comment, Form, message, Drawer
} from 'antd';
import Comments from './Comments.js';
import AjaxApi from '../api/AjaxApi.js'
import 'braft-editor/dist/output.css'

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
  }

  handleSubmit = (e) => {
    e.preventDefault();
    if (this.state.comment.length === 0 ||
      this.state.comment.trim().length === 0) {
      message.error('不能发布空评论');
    } else {
      console.log(this.state.comment)
    }
  }

  onDrawerClose = (e) => {
    this.setState({ showComments: false });
  }

  handleInputChange = (e) => {
    this.setState({ comment: e.target.value });
  }

  onCollectClick = () => {
    this.setState({
      collectAcitve: !this.state.collectAcitve,
      collectCount: this.state.collectCount + (this.state.collectAcitve ? -1 : 1)
    },
      () => {
        if (this.state.collectAcitve) {
          message.success("已添加到收藏夹");
        } else {
          message.success("已取消收藏");
        }
      })
  }

  onAgreeClick = () => {
    this.setState({
      agreeAcitve: !this.state.agreeAcitve,
      agreeCount: this.state.agreeCount + (this.state.agreeAcitve ? -1 : 1)
    },
      () => {
        if (this.state.agreeAcitve) {
          message.success("已赞同");
        } else {
          message.success("已取消赞同");
        }
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
                    onClick={() => this.onCollectClick()}
                  ><Icon type="star-o" style={{ marginRight: 8 }} />{this.state.collectCount} 收藏</span>,
                  <span className={this.state.agreeAcitve ? "active" : null}
                    onClick={() => this.onAgreeClick()}
                  ><Icon type="like-o" style={{ marginRight: 8 }} />{this.state.agreeCount} 赞同</span>,
                  <span onClick={() => this.setState({ showComments: true })}><Icon type="message" style={{ marginRight: 8 }} />{this.state.answer.comment.length} 评论</span>,
                  <span onClick={() => this.props.createAnswerClick({ key: item.qid })}><Icon type="edit" style={{ marginRight: 8 }} />撰写答案</span>
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
                src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                alt="Han Solo"
              />
            )}
            content={(
              <Input placeholder='输入评论内容，然后按下回车发布'
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
          <Comments />
        </Drawer>
      </div>
    );
  }
}

export default Answer;