$(document).ready(function(){
    var user = {};
    const token = localStorage.getItem('jwt');
    console.log(token);
    let isStudent = false;
    // 发送 AJAX 请求获取用户信息
    $.ajax({
        url: '/api/currentUser',
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token // 在请求头中附带 JWT
        },
        success: function(response) {
            console.log(response);
            localStorage.setItem('userId', response.id);
            // 处理用户信息
            const role = response.userType;
            let roleDisplay;
            user.id = response.id;
            user.username = response.username;

            user.email = response.email;
            user.studentNumber = response.studentNumber;
            user.avatarUrl = response.avatarUrl || 'img/default.jpg';

            user.nickname = response.nickname;
            user.signature = response.signature;
            // user.todoList = response.todoList;
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('role', role);

            let host = window.location.host;
            console.log(host);

            if (role === "TEACHER") {
                roleDisplay = "教师";
            } else if (role === "STUDENT") {
                roleDisplay = "学生";
            } else {
                roleDisplay = "游客";
            }
            if (response.username) {
                // 用户已登录，删除注册登录链接
                $('#loginNavItem').remove();
                // 在testNavItem和searchNavItem之间添加个人中心
                $('#testNavItem').after(`
                        <li class="nav-item">
                            <a class="nav-link" href="/me">个人中心</a>
                        </li>
                    `);
                // 动态添加用户信息和登出按钮
                $('#navItems').append(`

                        <img src="${"http://"+host+"/" + user.avatarUrl}" class="rounded-circle" style="width: 40px; height: 40px">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                ${response.username}
                            </a>
                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="/me">角色: ${roleDisplay}</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="/logout" id="logout">登出</a>
                            </div>
                        </li>
                    `);
                // 如果是学生，学生ID输入框显示用户名，否则显示当前非学生用户
                if (role === "STUDENT") {
                    $('#studentId').val(response.username);
                    isStudent = true;
                }

            }
        },
        error: function(xhr, status, error) {
            // 请求失败，不做额外处理，保持注册登录链接可见
        }
    });
    console.log(isStudent);
    if (!isStudent) {
        $('#studentId').val('非学生用户，无法提交实验');
    }
    // 点击登出按钮时，删除本地存储的 JWT
    $('#navItems').on('click', '#logout', function() {
        localStorage.removeItem('jwt');
    });
});