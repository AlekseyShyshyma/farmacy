import React, { Component } from 'react';
import { Form, Input, Button, Icon, notification, Row, Col } from 'antd';
import '../../styles/LoginPage.css';
import {addRow} from '../../apiUtils';

const FormItem = Form.Item;

class AddMedicine extends Component {
    constructor(props) {
        super(props);
    };

    render() {
        const AntWrappedLoginForm = Form.create()(AddMedicineForm);
        return (
            <div className="login-container">
                <div className="login-content">
                    <AntWrappedLoginForm onAdding={this.props.onAdding}/>
                </div>
            </div>
        );
    }
}

class AddMedicineForm extends Component {
    constructor(props) {
      super(props);
      this.handleSubmit = this.handleSubmit.bind(this);
    };

    handleSubmit(event) {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const newMedicine = Object.assign({}, values);
                console.log(newMedicine);
                addRow(newMedicine).then((res) => {
                    this.props.onAdding();
                });
            }
        });
    }

    render() {
        const { getFieldDecorator } = this.props.form;
        return(
          <div>
              <Form onSubmit={this.handleSubmit} className="login-form">
                  <Row>
                      <Col span={8}>
                          <FormItem>
                              {getFieldDecorator('medicineCode', {
                                  rules: [{ required: true, message: 'Please input medicine code!' }],
                              })(
                                  <Input
                                      prefix={<Icon type="user" />}
                                      size="large"
                                      name="medicineCode"
                                      placeholder="Medicine Code" />
                              )}
                          </FormItem>
                      </Col>

                      <Col span={8}>
                          <FormItem>
                              {getFieldDecorator('price', {
                                  rules: [{ required: true, message: 'Please input price!' }],
                              })(
                                  <Input
                                      prefix={<Icon type="lock" />}
                                      size="large"
                                      name="price"
                                      type="number"
                                      placeholder="Price"  />
                              )}
                          </FormItem>
                      </Col>
                      <Col span={8}>
                          <FormItem>
                              {getFieldDecorator('title', {
                                  rules: [{ required: true, message: 'Please input title!' }],
                              })(
                                  <Input
                                      prefix={<Icon type="lock" />}
                                      size="large"
                                      name="title"
                                      type="text"
                                      placeholder="Title"  />
                              )}
                          </FormItem>
                      </Col>
                  </Row>
                  <Row>
                      <Col span={8}>
                          <FormItem>
                              {getFieldDecorator('expirationTerm', {
                                  rules: [{ required: true, message: 'Please input expiration term!' }],
                              })(
                                  <Input
                                      prefix={<Icon type="lock" />}
                                      size="large"
                                      name="expirationTerm"
                                      type="text"
                                      placeholder="Term"  />
                              )}
                          </FormItem>
                      </Col>
                      <Col span={8}>
                          <FormItem>
                              {getFieldDecorator('measurementUnit', {
                                  rules: [{ required: true, message: 'Please input measurement unit!' }],
                              })(
                                  <Input
                                      prefix={<Icon type="lock" />}
                                      size="large"
                                      name="measurementUnit"
                                      type="text"
                                      placeholder="Unit"  />
                              )}
                          </FormItem>
                      </Col>
                      <Col span={8}>
                          <FormItem>
                              {getFieldDecorator('manufacturerCode', {
                                  rules: [{ required: true, message: 'Please input manufacturer code!' }],
                              })(
                                  <Input
                                      prefix={<Icon type="lock" />}
                                      size="large"
                                      name="manufacturerCode"
                                      type="number"
                                      placeholder="Manufacturer"  />
                              )}
                          </FormItem>
                      </Col>
                  </Row>
                  <FormItem>
                      <Button type="primary" htmlType="submit" size="large" className="login-form-button">Add</Button>
                  </FormItem>
              </Form>
          </div>
        );
    }
}

export default AddMedicine;
