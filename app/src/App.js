//eslint-disable import/first
import React, { Component } from 'react';
import { ACCESS_TOKEN } from './constants';
import { Route, withRouter, Switch } from 'react-router-dom';
import { getCurrentUser } from "./apiUtils";
import { Layout, notification } from 'antd';
import LoginPage from './pages/LoginPage';
import DownloadPage from './pages/DownloadPage';
import SignUp from './pages/SignUpPage';
import UploadPage from './pages/UploadPage';
import AppHeader from './components/AppHeader';
import Profile from './components/Profile';
import './styles/App.css';

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
          console.log(this.state.currentUser);
          this.props.history.push("/managers/" + this.state.currentUser.managerCode);
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
  handleLogout(redirectTo="/login", notificationType="success", description="You're successfully logged out.") {
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
      message: 'Pharmacy App',
      description: "You're successfully logged in.",
    });
    this.loadCurrentUser();
  }

  render() {
    console.log(this.state.currentUser);
    /*<PrivateRoute authenticated={this.state.isAuthenticated} path="/poll/new" component={NewPoll} handleLogout={this.handleLogout}/>*/
    return (
        <Layout className="app-container">
          <AppHeader isAuthenticated={this.state.isAuthenticated}
                     currentUser={this.state.currentUser}
                     onLogout={this.handleLogout} />

          <Content className="app-content">
            <div className="container">
              <Switch>
                <Route path="/login"
                       render={(props) => <LoginPage onLogin={this.handleLogin} {...props} />}/>
                <Route path="/signup" component={SignUp}/>
                <Route path="/managers/:username"
                       render={(props) => <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} isAdmin={false} {...props}/>}>
                </Route>
                <Route path="/upload"
                       render={(props) => <UploadPage {...props} />}>
                </Route>
                <Route path="/download"
                       render={(props) => <DownloadPage {...props} />}>
                </Route>
              </Switch>
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
