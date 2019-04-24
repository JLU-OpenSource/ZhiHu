import React from 'react';
import {
  List, Avatar, Icon, Empty, Typography, Pagination,
  Divider, message, Form, Input, Comment, Drawer
} from 'antd';
import 'braft-editor/dist/output.css'
import Comments from './Comments'
import AjaxApi from '../api/AjaxApi.js'

class Article extends React.Component {

  state = {
    article: this.props.article,
    content: '',
    data: [],
    page: 1,
    count: 0,
    collectAcitve: false,
    agreeAcitve: true,
    showComments: false,
  }

  async componentDidMount() {
    fetch(AjaxApi.host + "/api/article/count", {
      headers: {
        'token': sessionStorage.getItem('token'),
      }
    }).then(response => response.text())
      .then(text => this.setState({ count: parseInt(text) }))
    if (this.state.article == null) {
      this.pullArticle()
    } else {
      const data = [];
      data.push(this.state.article);
      this.setState({ data: data, page: this.state.article.id }, () => console.log(this.state.page));
      this.fetchContent();
      this.initMetadata()
    }
  }

  initMetadata = () => {
    const user = JSON.parse(sessionStorage.getItem('user'))
    const _article = this.state.article
    let collect = -1;
    for (let i = 0; i < _article.collect.length; i++) {
      if (_article.collect[i].id === user.id)
        collect = i;
    }
    let agree = -1;
    for (let i = 0; i < _article.agree.length; i++) {
      if (_article.agree[i].id === user.id)
        agree = i;
    }

    this.setState({
      collectAcitve: collect !== -1,
      agreeAcitve: agree !== -1
    })
  }

  pullArticle = () => {
    fetch(AjaxApi.host + "/api/article/" + this.state.page)
      .then(response => response.json())
      .then(response => {
        if (response !== null && response.body !== null) this.setState({ article: response.body },
          function () {
            const data = [];
            this.fetchContent();
            data.push(this.state.article);
            this.setState({ data: data, page: this.state.article.id })
            this.initMetadata()
          })
      })
  }

  onShowSizeChange = (currentPage) => {
    this.setState({ page: currentPage },
      () => this.pullArticle())
  }

  fetchContent = () => {
    fetch(AjaxApi.host + "/article/" + this.state.article.id + ".html")
      .then(response => response.text())
      .then(text => this.setState({ content: text }))
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

      AjaxApi.post('/api/comment/create', AjaxApi.body(comment, { id: this.state.article.id, type: 'article' })
      ).then(response => {
        if (response.status === 200) {
          message.success("成功发表评论")
          this.setState({ comment: '' })
          const article = this.state.article;
          article.comment.push(response.body);
          this.setState({ article: article })
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
    const _article = this.state.article;
    let index = -1;
    if (type) {
      for (let i = 0; i < _article.agree.length; i++) {
        if (_article.agree[i].id === user.id)
          index = i;
      }
      if (index === -1) {
        _article.agree.push(user)
      } else {
        _article.agree.splice(index, 1);
      }
      this.setState({
        article: _article,
        agreeAcitve: _article.agree.indexOf(user) !== -1,
      },
        () => {
          if (this.state.agreeAcitve) {
            message.success("已赞同");
          } else {
            message.success("已取消赞同");
          }
        })
    } else {
      for (let i = 0; i < _article.collect.length; i++) {
        if (_article.collect[i].id === user.id)
          index = i;
      }
      if (index === -1) {
        _article.collect.push(user)
      } else {
        _article.collect.splice(index, 1);
      }
      this.setState({
        article: _article,
        collectAcitve: _article.collect.indexOf(user) !== -1,
      },
        () => {
          if (this.state.collectAcitve) {
            message.success("已收藏");
          } else {
            message.success("已取消收藏");
          }
        })
    }

    AjaxApi.post('/api/article/metadata', AjaxApi.body(_article, {}))
      .then(response => {
        console.log(response)
      })
  }

  render() {
    return (
      <div>
        <Typography>
          <h3>{this.state.article == null ? "" : this.state.article.title}</h3>
        </Typography>

        <Divider style={{ margin: '0px' }} />
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
                    onClick={() => this.handleMetaDataClick(false)}><Icon type="star-o" style={{ marginRight: 8 }} />{this.state.article.collect.length} 收藏</span>,
                  <span className={this.state.agreeAcitve ? "active" : null}
                    onClick={() => this.handleMetaDataClick(true)}><Icon type="like-o" style={{ marginRight: 8 }} />{this.state.article.agree.length} 赞同</span>,
                  <span onClick={() => this.setState({ showComments: true })}><Icon type="message" style={{ marginRight: 8 }} />{item.comment.length} 评论</span>
                ]}>
              <List.Item.Meta
                avatar={<Avatar src={item.author.avatar} />}
                title={item.author.name}
                description={item.author.sign}
              />
              <Typography>
                <Divider />
                <div className="braft-output-content" dangerouslySetInnerHTML={{ __html: this.state.content }}></div>
                <Divider />
              </Typography>
            </List.Item>
          )
          }
        />
        <Form onSubmit={this.handleSubmit}>{this.state.article == null ? null :
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
        }
        </Form>
        <Drawer
          width={500}
          visible={this.state.showComments}
          title='评论'
          placement="right"
          onClose={this.onDrawerClose}
          closable={true}>
          {this.state.showComments ? <Comments id={this.state.page} type='article' /> : null}
        </Drawer>
        <Pagination simple pageSize={1} current={this.state.page} onChange={this.onShowSizeChange}
          total={this.state.count} style={{ marginTop: '30px', textAlign: 'center' }} />
      </div>
    );
  }
}

export default Article;