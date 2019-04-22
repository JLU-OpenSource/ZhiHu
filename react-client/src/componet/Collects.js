import React from 'react'
import { Table, message, Popconfirm, Empty } from 'antd';
import moment from "moment";
import AjaxApi from '../api/AjaxApi.js'

const data = [];
let localAnswer = [];
let localArticle = [];
class Collects extends React.Component {

  state = {
    data: [],
    empty: true
  }

  handleConfirmDel = (key, type) => {
    if (type === '回答')
      AjaxApi.post('/api/answer/removeCollect', AjaxApi.body(localAnswer[key - 1], {})
      ).then(response => {
        message.success("成功删除收藏")
        this.pullCollects();
      })
    else
      AjaxApi.post('/api/article/removeCollect', AjaxApi.body(localArticle[0 - key - 1], {})
      ).then(response => {
        message.success("成功删除收藏")
        this.pullCollects();
      })
  }

  pullCollects = () => {
    const _this = this;
    data.length = 0;
    localAnswer.length = 0;
    localArticle.length = 0;
    AjaxApi.post('/api/answer/collect', AjaxApi.body(JSON.parse(sessionStorage.getItem('user')), {})
    ).then(response => {
      if (response != null && response.body.length !== 0) {
        _this.setState({ empty: false });
        localAnswer = response.body;
        for (let i = 0; i < response.body.length; i++) {
          data.push({
            key: i + 1,
            title: response.body[i].title,
            time: moment(response.body[i].st).format('YYYY-MM-DD HH:mm:ss'),
            type: "回答",
          })
        }
      } else {
        _this.setState({ empty: true });
      }
      _this.setState({ data: data })
    })

    AjaxApi.post('/api/article/collect', AjaxApi.body(JSON.parse(sessionStorage.getItem('user')), {})
    ).then(response => {
      if (response != null && response.body.length !== 0) {
        _this.setState({ empty: false });
        localArticle = response.body;
        for (let i = 0; i < response.body.length; i++) {
          data.push({
            key: 0 - i - 1,
            title: response.body[i].title,
            time: moment(response.body[i].st).format('YYYY-MM-DD HH:mm:ss'),
            type: "文章",
          })
        }
      } else {
        _this.setState({ empty: true });
      }
      _this.setState({ data: data })
    })
  }

  async componentDidMount() {
    this.pullCollects();
  }

  render() {
    const columns = [
      {
        title: '标题',
        dataIndex: 'title',
        key: 'title',
      },
      {
        title: '日期',
        dataIndex: 'time',
        key: 'time',
      },
      {
        title: '类型',
        dataIndex: 'type',
        key: 'type',
      },
      {
        title: '操作',
        key: 'id',
        render: (record) => (
          <span>
            <Popconfirm title="要删除此收藏吗？此操作无法恢复。"
              onConfirm={() => this.handleConfirmDel(record.key, record.type)}>
              <a href='javascrpit:void(0)'>删除</a>
            </Popconfirm>
          </span>
        ),
      }];
    return (
      this.state.empty ? <Empty style={{ marginTop: '100px' }} /> :
        <Table columns={columns} dataSource={this.state.data} />
    );
  }
}

export default Collects;