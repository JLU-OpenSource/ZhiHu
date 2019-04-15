import React from 'react'
import { Table, Divider, message, Popconfirm, Empty } from 'antd';
import moment from "moment";
import DraftApi from '../api/DraftApi';

const data = [];
let localDrafts = [];
class Drafts extends React.Component {

  state = {
    data: [],
    empty: true
  }

  handleEdit = (key) => {
    this.props.restoreDraft(localDrafts[key])
  }

  handleConfirmDel = (key) => {
    const _this = this;
    DraftApi.remove(localDrafts[key], function (response) {
      if (response != null) {
        _this.pullDrafts();
        message.success("成功删除草稿")
      }
    });
  }

  pullDrafts = () => {
    const _this = this;
    data.length = 0;
    localDrafts.length = 0;
    DraftApi.getAll(function (drafts) {
      if (drafts != null && drafts.length !== 0) {
        _this.setState({ empty: false });
        localDrafts = drafts;
        for (let i = 0; i < drafts.length; i++) {
          data.push({
            key: i,
            title: drafts[i].title,
            time: moment(drafts[i].st).format('YYYY-MM-DD HH:mm:ss'),
            summary: drafts[i].summary,
          })
        }
      } else {
        _this.setState({ empty: true });
      }
      _this.setState({ data: data })
    });
  }

  async componentDidMount() {
    this.pullDrafts();
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
        title: '概要',
        dataIndex: 'summary',
        key: 'summary',
      },
      {
        title: '操作',
        key: 'id',
        render: (record) => (
          <span>
            <a href='javascrpit:void(0)' onClick={() => this.handleEdit(record.key)}>编辑</a>
            <Divider type="vertical" />
            <Popconfirm title="要删除此草稿吗？此操作无法恢复。"
              onConfirm={() => this.handleConfirmDel(record.key)}>
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

export default Drafts;