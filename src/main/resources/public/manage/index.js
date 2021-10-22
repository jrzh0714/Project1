import {getUser,getAll,User,Request,logOut} from '../dashboard/index.js'


const getAllEmployees=(user)=>{
  return fetch("http://localhost:8080/api/manager/allEmployees",{
      method:'POST',
      headers:{
          'Content-Type':'application/json'
      },
      body:JSON.stringify(user)
  });
}

const newEmployee=(user)=>{
  return fetch("http://localhost:8080/api/manager/newEmployee",{
      method:'POST',
      headers:{
          'Content-Type':'application/json'
      },
      body:JSON.stringify(user)
  });
}

function ValidateEmail(mail) 
{
 if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail))
  {
    return (true)
  }
    alert("You have entered an invalid email address!")
    return (false)
}
class newEmp {
  constructor(username,password,email,employee) {
    this.username = username || "";
    this.password = password || "";
    this.email = email || "";
    this.employee = employee || false;
  }

}
document.addEventListener("DOMContentLoaded", () => {

    var user;
    var allusers;
    var data = sessionStorage.getItem("username");
    const logout = document.querySelector(".logout");
    const logoutmenu = document.querySelector(".logoutmenu");
    const logoutbutton = document.querySelector(".logoutbutton");
    const newrequest = document.querySelector(".newrequestbutton");
    const requestform = document.querySelector(".requestform");
    const formclose = document.querySelector(".formclose");
    const submitemployee = document.querySelector("#submitemployee");
    const newempform = document.forms[0];
    const formcontainer = document.querySelector(".formcontainer");
    const employeelist = document.querySelector(".employeelist");
    const allrequests = document.querySelector(".allrequests");
    const addemployee = document.querySelector(".addemployee");
    const loader = document.querySelector("#loading");

    // showing loading
    function displayLoading() {
        loader.classList.add("display");
        // to stop loading after some time
        setTimeout(() => {
            loader.classList.remove("display");
        }, 5000);
    }
    
    // hiding loading 
    function hideLoading() {
        loader.classList.remove("display");
    }
    document.querySelector(".displayname").innerHTML= data;

    const body = employeelist.querySelector("tbody");
    var reqjson = [];
    var jsondata= [];
    
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
      getAllEmployees(user)
        .then(resp => {
          if (resp.status===201){
            resp.json().then(
              json => {
                json.forEach(element => {
                  console.log(element);
                  var row = body.insertRow(-1);
                  var cell1 = row.insertCell(0);
                  var cell2 = row.insertCell(1);
                  var cell3 = row.insertCell(2);
                  var cell4 = row.insertCell(3);

//          
                  cell1.innerHTML=element.user_id;
                  cell2.innerHTML=element.username;
                  cell3.innerHTML=element.email;
                  if(element.employee){
                    cell4.innerHTML="Manager";

                  } else{
                    cell4.innerHTML="Employee";

                  }

                });

              }
            )

          } else {
            alert("not a manager");
          }
        });
        
  
    });
  

    logoutbutton.addEventListener("click", (e) => {
        logOut().then(resp=>{
          console.log(resp);
          sessionStorage.clear();
          window.open(resp.url,"_self");
        });
      });
      addemployee.addEventListener("click", (e) => {
        console.log("add");
        
        document.querySelector(".newempform").classList.toggle("notvisible");
        document.querySelector(".formcontainer").classList.toggle("notvisible");
        document.querySelector(".employeelist").classList.toggle("notvisible");


      });
      submitemployee.addEventListener("click", (e) => {
        e.preventDefault();
        displayLoading();
        console.log("submit");
        const { username,email,usertype } = newempform.elements;
        console.log(username.value);
        console.log(email.value);
        console.log(usertype.value);
        var type = false;
        if(usertype.value ==="Manager"){
          type = true;
        }
        const newemp = new newEmp(
          
          username.value,
          "",
          email.value,
          type
        );
        console.log(newemp);
        console.log(ValidateEmail(email.value));
        newEmployee(newemp).then(resp=>{
          if (resp.status===201){
            hideLoading();
            alert("success");
            location.reload();
            console.log(resp);
          } else {
            hideLoading();
            alert("fail");
            newempform.elements[0].value="";
            newempform.elements[1].value="";
            newempform.elements[2].checked=false;

          }
  
      });


      });

      formclose.addEventListener("click", (e) => {
        document.querySelector(".newempform").classList.toggle("notvisible");
        document.querySelector(".formcontainer").classList.toggle("notvisible");
        document.querySelector(".employeelist").classList.toggle("notvisible");
    
      });

  

      


  });