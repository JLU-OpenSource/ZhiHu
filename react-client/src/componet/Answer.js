import React from 'react';
import {
  List, Avatar, Icon, Empty, Typography,
  Divider, Input, Comment, Form, message, Drawer
} from 'antd';
import Comments from './Comments.js';
const listData = [];
for (let i = 0; i < 1; i++) {
  listData.push(
    {
      href: '#',
      title: '什么是MarkDown？如何使用MarkDown？',
      author: {
        name: `知乎刘看山`,
        avatar: 'https://s2.ax1x.com/2019/04/02/A6ab3d.jpg',
        sign: '发现更大的世界',
      },
      content: 'Markdown 是一个 Web 上使用的文本到HTML的转换工具，可以通过简单、易读易写的文本格式生成结构化的HTML文档。目前 github、Stackoverflow 等网站均支持这种格式。',
    }
  );
}

const Content = () => {
  return (
    <Typography>
      <h1>介绍</h1>
      <p>
        蚂蚁的企业级产品是一个庞大且复杂的体系。这类产品不仅量级巨大且功能复杂，而且变动和并发频繁，
        常常需要设计与开发能够快速的做出响应。同时这类产品中有存在很多类似的页面以及组件，
        可以通过抽象得到一些稳定且高复用性的内容。
    </p>
      <p>
        随着商业化的趋势，越来越多的企业级产品对更好的用户体验有了进一步的要求。
        带着这样的一个终极目标，我们（蚂蚁金服体验技术部）经过大量的项目实践和总结，
        逐步打磨出一个服务于企业级产品的设计体系 Ant Design。基于『确定』和『自然』
        的设计价值观，通过模块化的解决方案，降低冗余的生产成本，让设计者专注于更好的用户体验。
    </p>
      <h2>设计资源</h2>
      <p>
        我们提供完善的设计原则、最佳实践和设计资源文件（Sketch 和 Axure），
        来帮助业务快速设计出高质量的产品原型。
    </p>
    </Typography>
  )
}

class Answer extends React.Component {

  state = {
    comment: '',
    showComments: false,
    collectAcitve: false,
    collectCount: 156,
    agreeAcitve: true,
    agreeCount: 156
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
          dataSource={listData}
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
                  <span onClick={() => this.setState({ showComments: true })}><Icon type="message" style={{ marginRight: 8 }} />156 评论</span>,
                  <span onClick={() => this.props.createAnswerClick(item)}><Icon type="edit" style={{ marginRight: 8 }} />撰写答案</span>
                ]}>
              <a href='javascrpit:void(0)'><h3>{item.title}</h3></a>
              <List.Item.Meta
                avatar={<Avatar src={item.author.avatar} />}
                title={<a href={item.href}>{item.author.name}</a>}
                description={item.author.sign}
              />
              <Divider />
              <Content />
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