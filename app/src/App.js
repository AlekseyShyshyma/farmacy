import React, { Component } from 'react';
import './styles/App.css';

import {
  Route,
  withRouter,
  Switch
} from 'react-router-dom';

import { ACCESS_TOKEN } from './constants';

import LoginPage from './pages/LoginPage';
import SignUp from './pages/SignUpPage';

import { Layout, notification } from 'antd';
const { Content } = Layout;

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
      isAuthenticated: false,
      isLoading: false
    };
    this.handleLogout = this.handleLogout.bind(this);
    this.loadCurrentUser = this.loadCurrentUser.bind(this);
    this.handleLogin = this.handleLogin.bind(this);

    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    });
  }

  loadCurrentUser() {
    this.setState({
      isLoading: true
    });
    getCurrentUser()
        .then(response => {
          this.setState({
            currentUser: response,
            isAuthenticated: true,
            isLoading: false
          });
        }).catch(error => {
      this.setState({
        isLoading: false
      });
    });
  }

  componentDidMount() {
    this.loadCurrentUser();
  }

  // Handle Logout, Set currentUser and isAuthenticated state which will be passed to other components
  handleLogout(redirectTo="/", notificationType="success", description="You're successfully logged out.") {
    localStorage.removeItem(ACCESS_TOKEN);

    this.setState({
      currentUser: null,
      isAuthenticated: false
    });

    this.props.history.push(redirectTo);

    notification[notificationType]({
      message: 'Pharmacy App',
      description: description,
    });
  }

  /*
   This method is called by the Login component after successful login
   so that we can load the logged-in user details and set the currentUser &
   isAuthenticated state, which other components will use to render their JSX
  */
  handleLogin() {
    notification.success({
      message: 'Polling App',
      description: "You're successfully logged in.",
    });
    this.loadCurrentUser();
    this.props.history.push("/");
  }

  render() {
    return (
        <Layout className="app-container">
          <Content className="app-content">
            <div className="container">
              <Switch>
                {/*<Route exact path="/"*/}
                       {/*render={(props) => <PollList isAuthenticated={this.state.isAuthenticated}*/}
                                                    {/*currentUser={this.state.currentUser} handleLogout={this.handleLogout} {...props} />}>*/}
                {/*</Route>*/}
                <Route path="/login"
                       render={(props) => <LoginPage onLogin={this.handleLogin} {...props} />}/>
                <Route path="/signup" component={SignUp}/>
                {/*<Route path="/drugstores/:managerCode"*/}
                       {/*render={(props) => <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} {...props}  />}>*/}
                {/*</Route>*/}
                /*<PrivateRoute authenticated={this.state.isAuthenticated} path="/poll/new" component={NewPoll} handleLogout={this.handleLogout}/>*/
                {/*<Route component={NotFound}/>*/}
              </Switch>
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
