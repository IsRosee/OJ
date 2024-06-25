$(document).ready(function () {
    flashTwoLists();
    // 绑定搜索框输入事件
    $('#experiment-search').on('input', function () {
        const searchTerm = $(this).val().toLowerCase();
        $('#experiment-list .experiment-item').each(function () {
            const text = $(this).text().toLowerCase();
            $(this).toggle(text.includes(searchTerm));
        });
    });

    $('#notification-search').on('input', function () {
        const searchTerm = $(this).val().toLowerCase();
        $('#notification-list .list-group-item').each(function () {
            const text = $(this).text().toLowerCase();
            $(this).toggle(text.includes(searchTerm));
        });
    });
    // 模拟动态更新内容
    // setTimeout(function () {
    //     $('#experiment-list').prepend('<a href="#" class="list-group-item list-group-item-action experiment-item">演示新实验问题</a>');
    //     $('#notification-list').prepend('<li class="list-group-item">演示新课程通知</li>');
    //     $('#notification').fadeIn().delay(3000).fadeOut();
    // }, 5000);
    // 检查刷新后是否有新的通知
    // TODO
});

async function flashTwoLists() {
    // 先清空列表
    $('#experiment-list').empty();
    $('#notification-list').empty();

    fetch('/api/experiments')
        .then(response => response.json())
        .then(data => {
            data.forEach(experiment => {
                $('#experiment-list').append(
                    `<a href="/experimentDetail/${experiment.id}" class="list-group-item list-group-item-action experiment-item" data-id="${experiment.id}">
                            ${experiment.title}
                         </a>`
                );
            });
        });

    // 获取课程通知列表
    fetch('/api/notifications')
        .then(response => response.json())
        .then(data => {
            data.forEach(notification => {
                $('#notification-list').append(
                    `<li class="list-group-item" data-id="${notification.id}">
                            ${notification.title}
<!--                            <button class="btn btn-danger btn-sm float-right delete-notification">删除</button>-->
                         </li>`
                );
            });
        });

    // 删除课程通知
    $(document).on('click', '.delete-notification', function (e) {
        e.stopPropagation();
        // 确认删除
        if (!confirm('确定要删除这个通知吗？')) {
            return;
        }
        const id = $(this).parent().data('id');
        fetch(`/api/notifications/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('jwt')
            }
        }).then(() => {
            $(this).parent().remove();
        }).catch(error => console.error('Error:', error));
    });

    // // 查看实验问题内容
    // $(document).on('click', '.experiment-item', function () {
    //     const id = $(this).data('id');
    //     // 显示实验问题详情的逻辑
    //     alert('查看实验问题: ' + id);
    // });
    //
    // // 查看课程通知内容
    // $(document).on('click', '.list-group-item', function () {
    //     const id = $(this).data('id');
    //     // 显示课程通知详情的逻辑
    //     alert('查看课程通知: ' + id);
    // });
}