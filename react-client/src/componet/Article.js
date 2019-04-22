import React from 'react';
import { List, Avatar, Icon, Empty, Typography, Pagination, Divider, message } from 'antd';
import 'braft-editor/dist/output.css'

import AjaxApi from '../api/AjaxApi.js'

class Article extends React.Component {

  state = {
    article: this.props.article,
    content: '',
    data: [],
    page: 1,
    count: 0,
    collectAcitve: false,
    agreeAcitve: true
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
      console.log(this.state.comment)
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
                  <span><Icon type="message" style={{ marginRight: 8 }} />{item.comment.length} 评论</span>
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
        <Pagination simple pageSize={1} current={this.state.page} onChange={this.onShowSizeChange}
          total={this.state.count} style={{ marginTop: '10px', textAlign: 'center' }} />
      </div>
    );
  }
}

export default Article;