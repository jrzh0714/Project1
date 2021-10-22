const getUser=()=>{
  return fetch("http://localhost:8080/api/user/getuser")
}

const getAll=(user)=>{
    return fetch("http://localhost:8080/api/request/getall",{
        method:'POST',
        headers:{
            'Content-Type':'application/json'
        },
        body:JSON.stringify(user)
    });
}

const logOut=()=>{
  return fetch("http://localhost:8080/api/user/logout",{
        method:'GET',
        redirect:'follow'
  })
}

const addRequest=(request)=>{
  return fetch("http://localhost:8080/api/request/add",{
    method:'POST',
    headers:{
      'Content-Type':'application/json'
    },
    body:JSON.stringify(request)
  });
}
class User {
  constructor(user_id,username,password,email,employee) {
    this.user_id = user_id || 0;
    this.username = username || "";
    this.password = password || "";
    this.email = email || "";
    this.employee = employee || false;
  }

}

class Request {
  constructor(requestor_id,amount,status,resolver_id,description) {
    this.requestor_id = requestor_id || 0;
    this.amount = amount || 0;
    this.status = status || "";
    this.resolver_id = resolver_id || 0;
    this.description = description || "";
  }

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
  const login1 = document.forms[0];
  const formcontainer = document.querySelector(".formcontainer");
  const reqlist = document.querySelector(".reqlist");
  const allrequests = document.querySelector(".allrequests");

  var body = reqlist.querySelector("tbody");

  
  document.querySelector(".displayname").innerHTML= data;
  for(var i=0;i<6;i++){
    var row = body.insertRow(-1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    var cell5 = row.insertCell(4);
    cell1.innerHTML="";
    cell2.innerHTML="";
    cell3.innerHTML="";
    cell4.innerHTML="";
    cell5.innerHTML="";
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
    getAll(user)
      .then(resp => resp.json())
      .then(json => {
        console.log(json);
        const firstSix = json.slice(0,6);

        firstSix.forEach((ele,index) =>{

          var r = body.querySelectorAll("tr");
          var rowsele = r[index];

          var cells = rowsele.querySelectorAll("td");
          var c1 =cells[0];
          var c2 =cells[1];
          var c3 =cells[2];
          var c4 =cells[3];
          var c5 =cells[4];
          c1.innerHTML=ele.req_id;
          c2.innerHTML=ele.requestor_id;
          c3.innerHTML=ele.amount;
          c4.innerHTML=ele.submitted_date;
          c5.innerHTML=ele.status;
          
        });
      });

  });

  newrequest.addEventListener("click", (e) => {
    requestform.classList.toggle("notvisible");
    formcontainer.classList.toggle("notvisible");

  });

  allrequests.addEventListener("click", (e) => {
    window.open("../requests","_self")

  });

  formclose.addEventListener("click", (e) => {
    requestform.classList.toggle("notvisible");
    formcontainer.classList.toggle("notvisible");

  });

  submitRequest.addEventListener("click", (e) => {
    e.preventDefault();

    //  Request constructor(requestor_id,amount,status,resolver_id,description) 

    const { amount,description } = login1.elements;
    const request = new Request(
      sessionStorage.getItem("user_id"),
      amount.value,
      "Pending",
      0,
      description.value
    );

    console.log(request);
    addRequest(request)
      .then(resp=>{
        if (resp.status===201){
          alert("success");
          console.log(resp);
        } else {
          alert("fail");
        }
        location.reload();

    });
  });

  logoutbutton.addEventListener("click", (e) => {
    logOut().then(resp=>{
      console.log(resp);
      sessionStorage.clear();
      window.open(resp.url,"_self");
    });
  });

});

export { getUser,getAll,User,Request,logOut,addRequest};
