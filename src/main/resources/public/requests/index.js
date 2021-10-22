import {getUser,getAll,User,Request,logOut,addRequest} from '../dashboard/index.js'

const updateRequest=(request)=>{
  return fetch("http://localhost:8080/api/request/update",{
    method:'POST',
    headers:{
      'Content-Type':'application/json'
    },
    body:JSON.stringify(request)
  });
}

class updateReq {
  constructor(req_id,requestor_id,amount,status,resolver_id,description) {
    this.req_id = req_id || 0;
    this.requestor_id = requestor_id || 0;
    this.amount = amount || 0;
    this.status = status || "";
    this.resolver_id = resolver_id || 0;
    this.description = description || "";
  }

}

const goToPage=(target,body,json)=>{
        var max = target*7;
        var min = max-7;
        var count = Object.keys(json).length;
        console.log(count);
        var new_tbody = document.createElement('tbody');
        const currentpage = document.querySelector(".currentpage");
        var old = document.querySelector('tbody');

        if (count != 0){
          var batch;
          console.log(min,max);
          batch = json.slice(min,max);
          console.log(batch);
          const pagetarget = document.querySelectorAll(".pageindex")[target-1];
          console.log(pagetarget);
          console.log(pagetarget.innerHTML);

          currentpage.classList.remove("currentpage");
          pagetarget.classList.add("currentpage");

          batch.forEach((ele,index) =>{

          var r = document.createElement("tr");
          console.log(r);
          for (var key in ele) {
            if (ele.hasOwnProperty(key)) {
                console.log(ele[key]);
                var cell = document.createElement("td");
                var data = document.createTextNode(ele[key]);
                cell.appendChild(data);
                r.appendChild(cell);
                
            
            }
          }
          if(sessionStorage.getItem("employee") == 'true' && ele.status==="Pending"){
            var cell6 = document.createElement("td");
            var cell7 = document.createElement("td");
            var button6 = document.createElement("button");
            var button7 = document.createElement("button");
           
            button6.innerHTML=button6.innerHTML +"<i class='material-icons '>check</i>";
            button7.innerHTML=button7.innerHTML +"<i class='material-icons '>close</i>";

            button6.addEventListener("click",(e)=>{
              console.log("approve");
              var target = e.target;
              var parent = target.parentElement.parentElement.parentElement.childNodes[0].innerHTML;
              console.log("reqid = "+parent);
              var updateReq1 = new updateReq(
                target.parentElement.parentElement.parentElement.childNodes[0].innerHTML,
                target.parentElement.parentElement.parentElement.childNodes[1].innerHTML,
                target.parentElement.parentElement.parentElement.childNodes[2].innerHTML,
                "Approved",
                sessionStorage.getItem("user_id"),
                target.parentElement.parentElement.parentElement.childNodes[6].innerHTML);
                console.log(updateReq1);
                updateRequest(updateReq1)
                .then(resp=>{
                  if (resp.status===201){
                    alert("success");
                  } else {
                    alert("fail");
                  }
                  location.reload();
          
                });

            });
            button7.addEventListener("click",(e)=>{
              console.log("deny");
              var target = e.target;
              var parent = target.parentElement.parentElement.parentElement.childNodes[0].innerHTML;
              console.log("reqid = "+parent);
              var updateReq1 = new updateReq(
                target.parentElement.parentElement.parentElement.childNodes[0].innerHTML,
                target.parentElement.parentElement.parentElement.childNodes[1].innerHTML,
                target.parentElement.parentElement.parentElement.childNodes[2].innerHTML,
                "Denied",
                sessionStorage.getItem("user_id"),
                target.parentElement.parentElement.parentElement.childNodes[6].innerHTML);
                console.log(updateReq1);
                updateRequest(updateReq1)
                  .then(resp=>{
                  if (resp.status===201){
                    alert("success");
                  } else {
                    alert("fail");
                  }
                  location.reload();
          
                });
            });
            button6.classList.add("approvereq");
            button7.classList.add("denyreq");
            cell6.appendChild(button6);
            cell7.appendChild(button7);

            r.appendChild(cell6);
            r.appendChild(cell7);
          }

          new_tbody.appendChild(r);
        });

        } else{
          var r = document.createElement("tr");
          var cell = document.createElement("td");
          var data = document.createTextNode("No requests found.");
          cell.appendChild(data);
          r.appendChild(cell);
          new_tbody.appendChild(r);

        }
        document.querySelector(".fullreqlist").replaceChild(new_tbody, old)


}

const generatePageList =(json)=>{
  const reqlist = document.querySelector(".fullreqlist");
    const body = reqlist.querySelector("tbody");
  var count = Object.keys(json).length;
  
  console.log(count);
  var pagenum = Math.ceil(count / 8);
  var newpagelist = document.createElement("ol");
    for (var i=1; i<=pagenum;i++){
      var entry = document.createElement('li');
      entry.classList.add("pageindex");
      if(i==1){
        entry.classList.add("currentpage");
      }
      entry.appendChild(document.createTextNode(i));
      newpagelist.appendChild(entry);
      
    }
    var old = document.querySelector('#pagelist');

    document.querySelector(".pages").replaceChild(newpagelist, old)
    newpagelist.setAttribute("id", "pagelist");
    document.getElementById("pagelist").addEventListener("click",function(e) {
      // e.target is our targetted element.
                  // try doing console.log(e.target.nodeName), it will result LI
      if(e.target && e.target.nodeName == "LI") {
          console.log(e.target + " was clicked");
          console.log(e.target.innerHTML);
          goToPage(e.target.innerHTML,body,json);

      }
    });
}

document.addEventListener("DOMContentLoaded", () => {
  document.querySelector(".preload").classList.remove("preload");

    var user;
    var data = sessionStorage.getItem("username");
    const logout = document.querySelector(".logout");
    const logoutmenu = document.querySelector(".logoutmenu");
    const logoutbutton = document.querySelector(".logoutbutton");
    const newrequest = document.querySelector(".newrequestbutton");
    const requestform = document.querySelector(".requestform");
    const formclose = document.querySelector(".formclose");
    const submitButton = document.querySelector("#submit-button");
    const submitRequest = document.querySelector("#submit-button");
    const addreq = document.forms[0];
    const formcontainer = document.querySelector(".formcontainer");
    const reqlist = document.querySelector(".fullreqlist");
    const allrequests = document.querySelector(".allrequests");
    const body = reqlist.querySelector("tbody");
    const pagelist = document.querySelector("#pagelist");
    const requesttypes = document.querySelector('#requesttypes');
    const searchbyid = document.querySelector('#searchbyid');

    var reqjson = [];
    var jsondata= [];

    console.log(data);
    document.querySelector(".displayname").innerHTML= data;
    for(var i=0;i<8;i++){
        var row = body.insertRow(-1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        var cell3 = row.insertCell(2);
        var cell4 = row.insertCell(3);
        var cell5 = row.insertCell(4);
        var cell6 = row.insertCell(5);
        var cell7 = row.insertCell(6);
        var cell8 = row.insertCell(7);
        var cell9 = row.insertCell(8);
        cell1.innerHTML="";
        cell2.innerHTML="";
        cell3.innerHTML="";
        cell4.innerHTML="";
        cell5.innerHTML="";
        cell6.innerHTML="";
        cell7.innerHTML="";
        cell8.innerHTML="";
        cell9.innerHTML="";
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
        reqjson = json;
        jsondata = json;
        
        generatePageList(json);
        goToPage(1,body,json);
        
      });
      
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

    newrequest.addEventListener("click", (e) => {
        requestform.classList.toggle("notvisible");
        formcontainer.classList.toggle("notvisible");
    });
    
    formclose.addEventListener("click", (e) => {
        requestform.classList.toggle("notvisible");
        formcontainer.classList.toggle("notvisible");
    
    });


    submitRequest.addEventListener("click", (e) => {
      e.preventDefault();
  
      //  Request constructor(requestor_id,amount,status,resolver_id,description) 
  
      const { amount,description } = addreq.elements;
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

    requesttypes.addEventListener('change', (event) => {
      console.log(event.target.value);
      if (event.target.value === "All"){
        console.log("all")
        jsondata = reqjson;
      } else if(event.target.value === "Resolved") {
        console.log("Resolved")
        jsondata = reqjson.filter(function (e) {
          return e.status === "Approved"||e.status  === "Denied";
        }
        );
     } else{
      console.log("Pending")
      jsondata = reqjson.filter(function (e) {
        return e.status === "Pending";
      });
     }
      console.log(jsondata);
      generatePageList(jsondata);
      goToPage(1,body,jsondata);

    });
    
    searchbyid.addEventListener('keyup', (event) => {
      console.log(event.target.value);
      if (event.target.value == ""){
        console.log("empty")
        jsondata = reqjson;
      } else if(event.target.value === null && sessionStorage.getItem("employee") == false){
      console.log("id search for emp")
      jsondata = reqjson.filter(function (e) {
        return e.resolver_id == event.target.value && e.requestor_id == sessionStorage.getItem("user_id");
      });
     } else{
      console.log("id search for manager")
      jsondata = reqjson.filter(function (e) {
        return e.requestor_id == event.target.value || e.resolver_id == event.target.value;
      });
     }
      console.log(jsondata);
      generatePageList(jsondata);
      goToPage(1,body,jsondata);

    });

  
  });

 