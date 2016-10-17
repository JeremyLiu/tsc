function loginSubmit(){
    var name = $('#name').val();
    var password = $('#password').val();
    var postData = new FormData();
    postData.append('username', name);
    postData.append('password', password);
    $.ajax({
        url: 'user/login',
        method: 'POST',
        data: postData,
        contentType: false,
        mimeType: "multipart/form-data",
        processData:false,
        dataType: 'json',
        success: function(res){
            if(res.status === 0){
                location.reload();
            }else
                alert(res.message);
        },

        error: function(){
          alert('网络错误');
        }

    });
}

$('#loginBtn').click(loginSubmit);

$('#password').keydown(function(event){
    if(event.keyCode == 13)
        loginSubmit();
});