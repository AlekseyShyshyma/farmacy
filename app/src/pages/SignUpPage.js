import React, { Component } from 'react';
import { signup, checkUsernameAvailability} from '../apiUtils';
import {PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, USERNAME_MAX_LENGTH, USERNAME_MIN_LENGTH} from '../constants';
import { Link } from 'react-router-dom';

import { Form, Input, Button, notification } from 'antd';
import '../styles/SignUp.css';

const FormItem = Form.Item;

class Signup extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: {
                value: ''
            },
            surname: {
                value: ''
            },
            code: {
                value: ''
            },
            patronymic: {
                value: ''
            },
            phone: {
                value: ''
            },
            corporatePhone: {
                value: ''
            },
            address: {
                value: ''
            },
            position: {
                value: ''
            },
            password: {
                value: ''
            }
        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.validateUsernameAvailability = this.validateUsernameAvailability.bind(this);
        this.isFormInvalid = this.isFormInvalid.bind(this);
    }

    handleInputChange(event, validationFun) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : {
                value: inputValue,
                ...validationFun(inputValue)
            }
        });
    }

    handleSubmit(event) {
        event.preventDefault();

        const signupRequest = {
            managerCode: this.state.code.value,
            password: this.state.password.value,
            name: this.state.name.value,
            surname: this.state.surname.value,
            patronymic: this.state.patronymic.value,
            address: this.state.address.value,
            phoneNumber: this.state.phone.value,
            corporatePhoneNumber: this.state.corporatePhone.value,
            position: this.state.position.value,
        };
        signup(signupRequest)
            .then(response => {
                notification.success({
                    message: 'Polling App',
                    description: "Thank you! You're successfully registered. Please Login to continue!",
                });
                this.props.history.push("/login");
            }).catch(error => {
            notification.error({
                message: 'Polling App',
                description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
        });
    }

    isFormInvalid() {
        return !(this.state.name.validateStatus === 'success' &&
            this.state.code.validateStatus === 'success' &&
            this.state.surname.validateStatus === 'success' &&
            this.state.password.validateStatus === 'success' &&
            this.state.patronymic.validateStatus === 'success' &&
            this.state.phone.validateStatus === 'success' &&
            this.state.corporatePhone.validateStatus === 'success' &&
            this.state.position.validateStatus === 'success'
        );
    }

    render() {
        return (
            <div className="signup-container">
                <h1 className="page-title">Sign Up</h1>
                <div className="signup-content">
                    <Form onSubmit={this.handleSubmit} className="signup-form">
                        <FormItem
                            label="Name"
                            validateStatus={this.state.name.validateStatus}
                            help={this.state.name.errorMsg}>
                            <Input
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder="Your full name"
                                value={this.state.name.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem
                            label="Surname"
                            validateStatus={this.state.name.validateStatus}
                            help={this.state.surname.errorMsg}>
                            <Input
                                size="large"
                                name="surname"
                                autoComplete="off"
                                placeholder="Your full name"
                                value={this.state.surname.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem
                            label="Patronymic"
                            hasFeedback
                            validateStatus={this.state.patronymic.validateStatus}
                            help={this.state.patronymic.errorMsg}>
                            <Input
                                size="large"
                                name="patronymic"
                                type="text"
                                autoComplete="off"
                                placeholder="Your patronymic"
                                value={this.state.patronymic.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem label="Manager Code"
                                  hasFeedback
                                  validateStatus={this.state.code.validateStatus}
                                  help={this.state.code.errorMsg}>
                            <Input
                                size="large"
                                name="code"
                                autoComplete="off"
                                placeholder="A unique code"
                                value={this.state.code.value}
                                onBlur={this.validateUsernameAvailability}
                                onChange={(event) => this.handleInputChange(event, this.validateUsername)} />
                        </FormItem>
                        <FormItem
                            label="Address"
                            hasFeedback
                            validateStatus={this.state.address.validateStatus}
                            help={this.state.address.errorMsg}>
                            <Input
                                size="large"
                                name="address"
                                type="text"
                                autoComplete="off"
                                placeholder="Your email"
                                value={this.state.address.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem
                            label="Phone"
                            hasFeedback
                            validateStatus={this.state.phone.validateStatus}
                            help={this.state.phone.errorMsg}>
                            <Input
                                size="large"
                                name="phone"
                                type="text"
                                autoComplete="off"
                                placeholder="Phone"
                                value={this.state.phone.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem
                            label="Corporate Phone"
                            hasFeedback
                            validateStatus={this.state.corporatePhone.validateStatus}
                            help={this.state.corporatePhone.errorMsg}>
                            <Input
                                size="large"
                                name="corporatePhone"
                                type="text"
                                autoComplete="off"
                                placeholder="Corporate Phone"
                                value={this.state.corporatePhone.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem
                            label="Position"
                            hasFeedback
                            validateStatus={this.state.position.validateStatus}
                            help={this.state.position.errorMsg}>
                            <Input
                                size="large"
                                name="position"
                                type="text"
                                autoComplete="off"
                                placeholder="Position"
                                value={this.state.position.value}
                                onChange={(event) => this.handleInputChange(event, this.validateString)} />
                        </FormItem>
                        <FormItem
                            label="Password"
                            validateStatus={this.state.password.validateStatus}
                            help={this.state.password.errorMsg}>
                            <Input
                                size="large"
                                name="password"
                                type="password"
                                autoComplete="off"
                                placeholder="A password between 6 to 20 characters"
                                value={this.state.password.value}
                                onChange={(event) => this.handleInputChange(event, this.validatePassword)} />
                        </FormItem>
                        <FormItem>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className="signup-form-button"
                                    disabled={this.isFormInvalid()}>Sign up</Button>
                            Already registered? <Link to="/login">Login now!</Link>
                        </FormItem>
                    </Form>
                </div>
            </div>
        );
    }

    // Validation Functions

    validateName = (name) => {
        if(!name) {
            return {
                validateStatus: 'error',
                errorMsg: 'Name may not be empty'
            }
        }

        return {
            validateStatus: "success",
            errorMsg: null
        }
    };

    validateUsername = (username) => {
        if(username.length < USERNAME_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Username is too short (Minimum ${USERNAME_MIN_LENGTH} characters needed.)`
            }
        } else if (username.length > USERNAME_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Username is too long (Maximum ${USERNAME_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null
            }
        }
    };

    validateUsernameAvailability() {
        // First check for client side errors in username
        const usernameValue = this.state.code.value;
        const usernameValidation = this.validateUsername(usernameValue);

        if(usernameValidation.validateStatus === 'error') {
            this.setState({
                username: {
                    value: usernameValue,
                    ...usernameValidation
                }
            });
            return;
        }

        this.setState({
            username: {
                value: usernameValue,
                validateStatus: 'validating',
                errorMsg: null
            }
        });

        checkUsernameAvailability(usernameValue)
            .then(response => {
                if(response.available) {
                    this.setState({
                        username: {
                            value: usernameValue,
                            validateStatus: 'success',
                            errorMsg: null
                        }
                    });
                } else {
                    this.setState({
                        username: {
                            value: usernameValue,
                            validateStatus: 'error',
                            errorMsg: 'This username is already taken'
                        }
                    });
                }
            }).catch(error => {
            // Marking validateStatus as success, Form will be recchecked at server
            this.setState({
                username: {
                    value: usernameValue,
                    validateStatus: 'success',
                    errorMsg: null
                }
            });
        });
    }

    validatePassword = (password) => {
        if(password.length < PASSWORD_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
            }
        } else if (password.length > PASSWORD_MAX_LENGTH) {
            return {
                validationStatus: 'error',
                errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
            }
        } else {
            return {
                validateStatus: 'success',
                errorMsg: null,
            };
        }
    };

    validateString = (email) => {
        if(!email) {
            return {
                validateStatus: 'error',
                errorMsg: 'Email may not be empty'
            }
        }

        return {
            validateStatus: 'success',
            errorMsg: null
        }
    }

}

export default Signup;