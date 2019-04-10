const UserApi = {
    users: [],
    login: function (user) {
        return this.users.indexOf(user) !== -1;
    },
    register: function (user) {
        this.users.push(user);
    }
}

export default UserApi;