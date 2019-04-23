import React from 'react';
import { Form, Icon, Input, Button, Collapse, message } from 'antd';
import UserApi from '../../api/UserApi';

const Panel = Collapse.Panel;

class LoginForm extends React.Component {

  state = {
    collapseInfo: '注册新用户',
    btnText: '登陆',
    btnLoading: false,
    register: false,
    formValue: {}
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const _this = this;
    this.setState({ btnLoading: true })
    if (this.state.register) {
      UserApi.register(this.state.formValue, function callback(success) {
        if (success)
          window.location.reload();
        else
          message.error('注册失败,邮件地址已被使用');
        _this.setState({ btnLoading: false })
      });
    } else {
      UserApi.login(this.state.formValue, function callback(success) {
        if (success)
          window.location.reload();
        else
          message.error('用户名或密码错误');
        _this.setState({ btnLoading: false })
      });
    }
  }

  handleCollapseChange = (e) => {
    this.setState({
      register: e.length > 0,
      collapseInfo: e.length > 0 ? '返回登陆' : '注册新用户',
      btnText: e.length > 0 ? '注册' : '登陆'
    })
  }

  handleInputChange = (name, e) => {
    let input = this.state.formValue;
    input[name] = e.target.value;
    this.setState({ formValue: input });
  }

  render() {
    return (
      <Form id='login-form' onSubmit={this.handleSubmit}>
        <Form.Item>
          <Input allowClear required size='large' type="email" placeholder="邮箱地址"
            onChange={(e) => { this.handleInputChange('email', e) }}
            prefix={<Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />}
          />
        </Form.Item>
        <Form.Item style={{ marginBottom: '0px' }}>
          <Input.Password allowClear required size='large' placeholder="输入密码"
            onChange={(e) => { this.handleInputChange('password', e) }}
            prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
          />
        </Form.Item>
        <Form.Item style={{ marginBottom: '0px' }}>
          <Collapse bordered={false} onChange={this.handleCollapseChange}>
            <Panel header={this.state.collapseInfo} showArrow={false}>
              <Form.Item>
                <Input required={this.state.register} allowClear
                  onChange={(e) => { this.handleInputChange('name', e) }}
                  size='large' type="text" placeholder="用户昵称"
                  prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)', padding: '0px' }} />} />
              </Form.Item>
              <Form.Item>
                <Input allowClear
                  onChange={(e) => { this.handleInputChange('avatar', e) }}
                  size='large' type="url" placeholder="头像链接(可选)"
                  prefix={<Icon type="smile" style={{ color: 'rgba(0,0,0,.25)', padding: '0px' }} />} />
              </Form.Item>
              <Form.Item>
                <Input allowClear
                  onChange={(e) => { this.handleInputChange('site', e) }}
                  size='large' type="url" placeholder="个人站点(可选)"
                  prefix={<Icon type="cloud" style={{ color: 'rgba(0,0,0,.25)', padding: '0px' }} />} />
              </Form.Item>
            </Panel>
          </Collapse>
        </Form.Item>
        <Form.Item>
          <Button size='large' type="primary" htmlType="submit"
            block loading={this.state.btnLoading}>
            {this.state.btnText}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default LoginForm