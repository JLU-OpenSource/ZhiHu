import React from 'react';
import { List, Avatar, Icon, Empty, Typography, Pagination, Divider } from 'antd';
import 'braft-editor/dist/output.css'

import AjaxApi from '../api/AjaxApi.js'

class Article extends React.Component {

  state = {
    article: this.props.article,
    content: '',
    data: [],
    page: 1,
    count: 0
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
    }
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

  render() {
    return (
      <div>
        <Typography>
          <h3>{this.state.article == null ? "" : this.state.article.title}</h3>
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
                    <span><Icon type="star-o" style={{ marginRight: 8 }} />{item.collect.length} 收藏</span>,
                    <span><Icon type="like-o" style={{ marginRight: 8 }} />{item.agree.length} 赞同</span>,
                    <span><Icon type="message" style={{ marginRight: 8 }} />{item.comment.length} 评论</span>
                  ]}>
                <List.Item.Meta
                  avatar={<Avatar src={item.author.avatar} />}
                  title={item.author.name}
                  description={item.author.sign}
                />
                <Divider />
                <div className="braft-output-content" dangerouslySetInnerHTML={{ __html: this.state.content }}></div>
                <Divider />
              </List.Item>
            )
            }
          />
          <Pagination simple pageSize={1} current={this.state.page} onChange={this.onShowSizeChange}
            total={this.state.count} style={{ marginTop: '10px', textAlign: 'center' }} />
        </Typography>
      </div>
    );
  }
}

export default Article;