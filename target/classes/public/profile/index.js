import {getUser,getAll,User,Request,logOut} from '../dashboard/index.js'

const editInfo=(user)=>{
  return fetch("http://localhost:8080/api/user/edit",{
      method:'POST',
      headers:{
          'Content-Type':'application/json'
      },
      body:JSON.stringify(user)
  });
}

const changePassword=(newpass)=>{
  return fetch("http://localhost:8080/api/user/changePassword",{
      method:'POST',
      headers:{
          'Content-Type':'application/json'
      },
      body:JSON.stringify(newpass)
  });
}


document.addEventListener("DOMContentLoaded", () => {

    var user;
    var data = sessionStorage.getItem("username");
    const logout = document.querySelector(".logout");
    const logoutmenu = document.querySelector(".logoutmenu");
    const logoutbutton = document.querySelector(".logoutbutton");
    const newrequest = document.querySelector(".newrequestbutton");
    const requestform = document.querySelector(".requestform");
    const formclose = document.querySelector(".formclose");
    const submitRequest = document.querySelector("#submit-button");
    const detail = document.forms[0];
    const formcontainer = document.querySelector(".formcontainer");
    const reqlist = document.querySelector(".reqlist");
    const allrequests = document.querySelector(".allrequests");
    const editprofile = document.querySelector("#prof");
    const changepass = document.querySelector("#pw");

    const cancelbutton = document.querySelector("#cancelbutton");
    const savebutton = document.querySelector("#savebutton");
    const pwcancelbutton = document.querySelector("#pwcancelbutton");
    const pwsavebutton = document.querySelector("#pwsavebutton");
    const oldpass = document.querySelector("#oldpass");
    const togglePassword = document.querySelector('#togglePassword');

    const newpass = document.querySelector("#newpass");
    const confirmnewpass = document.querySelector("#confirmnewpass");
    document.querySelector(".displayname").innerHTML= data;
    const loader1 = document.querySelector("#loading1");

    // showing loading
    function displayLoading() {
        loader1.classList.add("display");
        // to stop loading after some time
        setTimeout(() => {
            loader1.classList.remove("display");
        }, 5000);
    }
    
    // hiding loading 
    function hideLoading() {
        loader1.classList.remove("display");
    }
    getUser()
    .then(resp => resp.json())
    .then(json => {
      console.log(json);
      console.log(json.user_id);
      sessionStorage.setItem("user_id",json.user_id);
      sessionStorage.setItem("email",json.email);
      sessionStorage.setItem("employee",json.employee);
      user = new User(json.user_id,json.username,json.password,json.email,json.employee);
      console.log(user);
      if(json.employee){
        document.querySelectorAll(".sidebaritem")[3].classList.remove("notvisible");
      }
      document.querySelector("#userid").value = json.user_id;
      document.querySelector("#username").value =json.username;
      document.querySelector("#userpassword").value ="•••••••••••••";
      document.querySelector("#useremail").value =json.email;
      if(json.status){
        document.querySelector("#userstatus").value ="Manager";
//
      }else{
        document.querySelector("#userstatus").value ="Employee";
//
      }

    });
  

    logoutbutton.addEventListener("click", (e) => {
        logOut().then(resp=>{
          console.log(resp);
          sessionStorage.clear();
          window.open(resp.url,"_self");
        });
      });
      editprofile.addEventListener("click", (e) => {
        console.log("edit");
        document.querySelector("#username").toggleAttribute("readonly");
        document.querySelector("#useremail").toggleAttribute("readonly");
        document.querySelector("#username").classList.toggle("uneditable");
        document.querySelector("#useremail").classList.toggle("uneditable");
        document.querySelector(".buttonrow").classList.toggle("notvisible");

      });

      cancelbutton.addEventListener("click", (e) => {
        e.preventDefault();

        console.log("cancel");
        
        document.querySelector("#username").classList.toggle("uneditable");
        document.querySelector("#useremail").classList.toggle("uneditable");
        document.querySelector(".buttonrow").classList.toggle("notvisible");

      });

      savebutton.addEventListener("click", (e) => {
        displayLoading();

        e.preventDefault();
        
        const updateuser = new User(
        sessionStorage.getItem("user_id"),
        document.querySelector("#username").value,
        user.password,
        document.querySelector("#useremail").value,
        user.employee
        );

        console.log(updateuser);

        editInfo(updateuser)
          .then(resp=>{
            if (resp.status===201){
              hideLoading();
              alert("success");
              sessionStorage.setItem("username",updateuser.username);
              sessionStorage.setItem("email",updateuser.email);
              console.log(resp);
            } else {
              hideLoading();
              alert("fail");
            }
            location.reload();
          
        });

      });
      
      changepass.addEventListener("click", (e) => {
      
        document.querySelector(".userdetailform").classList.toggle("notvisible");
        document.querySelector(".changepwform").classList.toggle("notvisible");
        document.querySelector(".pwbuttonrow").classList.toggle("notvisible");

      });
      pwcancelbutton.addEventListener("click", (e) => {
        e.preventDefault();

        console.log("cancel");
        document.getElementById("newpass").value = "";
        document.getElementById("oldpass").value = "";
        document.getElementById("confirmnewpass").value = "";
        document.querySelector(".userdetailform").classList.toggle("notvisible");
        document.querySelector(".changepwform").classList.toggle("notvisible");
        document.querySelector(".pwbuttonrow").classList.toggle("notvisible");

      });

      confirmnewpass.addEventListener("change",(e)=>{

      });
      newpass.addEventListener("change",(e)=>{
      });

      pwsavebutton.addEventListener("click", (e) => {
        e.preventDefault();
        displayLoading();

        console.log("save");
        var bool1 = newpass.value!=="" && (newpass.value === confirmnewpass.value);
        var bool2 = oldpass.value!=="" && oldpass.value === user.password;
        console.log(bool1);
        console.log(bool2);
        if( bool1 && bool2){
          changePassword(newpass.value).then(resp=>{
            if (resp.status===201){
              hideLoading();
              alert("success");
              location.reload();

            } else {
              hideLoading();
              alert("fail");
            }
          
          });

        }else if (!bool1){
          console.log("fail bool1");
          
        } else{
          console.log("fail bool2");

        }

        document.getElementById("newpass").value = "";
        document.getElementById("oldpass").value = "";
        document.getElementById("confirmnewpass").value = "";

      });

      togglePassword.addEventListener('click', function (e) {
        // toggle the type attribute
        const password = document.querySelectorAll(".pwinput");
        
        password.forEach(element=>{
          var type = element.getAttribute('type') === 'password' ? 'text' : 'password';
          element.setAttribute('type', type);
        // toggle the eye / eye slash icon
        });
        this.classList.toggle('bi-eye');
    });

  });