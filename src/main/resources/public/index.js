const hasContent = (element) => {
  return element.value;
};

const loginredirect=()=>{
  return fetch("http://localhost:8080/api/user/loginsuccess",{
      method:'GET',
      redirect:'follow'
  })
}
const login=(user)=>{
  return fetch("http://localhost:8080/api/user/login",{
    method:'POST',
    headers:{
      'Content-Type':'application/json'
    },
    body:JSON.stringify(user)
  });
}

class User {
  constructor(username,password) {
    this.username = username || "";
    this.password = password || "";
  }
  
}

document.addEventListener("DOMContentLoaded", () => {
  if(sessionStorage.getItem("username")!=null&& sessionStorage.getItem("user_id")!=null){
    console.log("already logged in");
    window.open("/dashboard","_self")

  }
  const submitButton = document.querySelector("#submit-button");
  const login1 = document.forms[0];

  submitButton.addEventListener("click", (e) => {
    e.preventDefault();

    // use obj destructuring to make value access easier
    const { username,password } = login1.elements;
    const user = new User(
      username.value,
      password.value
    );
    login(user)
        .then(resp=>{
          if (resp.status===201){
            alert("success");
              console.log(resp);
              sessionStorage.setItem("username",user.username);

              loginredirect().then(resp=>{
                console.log(resp);
                window.open(resp.url,"_self")
              });
          } else {
            alert("fail");
            location.reload();

          }
        });
    
    
  });
});
