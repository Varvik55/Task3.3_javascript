$(async function () {
    await getAllUsers();
    getUser()
})

// получение всех юзеров
async function getAllUsers() {
    let table = $('#AllUsers tbody');
    table.empty();

    fetch('/api/admin')
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tabContent = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.lastname}</td>
                            <td>${user.age}</td>
                             <td>${user.roles.map(role => role.name).join(" ")}</td>
                            <td>
                                <button type="button" class="btn btn-success" data-toggle="modal" onclick="editModal(${user.id})"  
                                data-target="#modalEdit" >Edit</button>                                
                            </td> 
                            <td>
                                <button type="button" class="btn btn-danger" data-toggle="modal" onclick="deleteModal(${user.id})" 
                                data-target="#modalDelete">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tabContent);
            })
        })

}
function getUser() {
    let table = $('#CurrentUser tbody');
    table.empty();

    fetch("http://localhost:8080/api/admin/info").then(res => res.json())
        .then(data => {
            let currentUser = data;
            console.log(data)
            console.log(JSON.stringify(currentUser, null, 2));

            let tabContent = `
                        <tr>
                            <td>${currentUser.id}</td>
                            <td>${currentUser.username}</td>
                            <td>${currentUser.lastname}</td>
                            <td>${currentUser.age}</td>
                             <td>${currentUser.roles.map(role => role.name).join(" ")}</td>
                        </tr>
                `;
            table.append(tabContent);

            document.getElementById("headerUsername").innerText = currentUser.username;
            document.getElementById("headerUserRoles").innerText = currentUser.roles.map(role => role.name).join(" ");
        })
}


let addForm = document.getElementById('formNew')
addForm.addEventListener('submit', addUser)
addForm.addEventListener('submit', successAdd)

function successAdd() {
    alert("User added!")
}


function addUser() {
    let roles = [];
    for (let i = 0; i < addForm.roles.options.length; i++) {
        if (addForm.roles.options[i].selected) roles.push({
            id: addForm.roles.options[i].value,
            role: addForm.roles.options[i].text
        });
    }

    fetch("/api/admin", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: addForm.username.value,
            lastname: addForm.lastname.value,
            age: addForm.age.value,
            password: addForm.password.value,
            roles: roles
        })
    }).then(() =>
        getAllUsers()
    );
}


async function getOneUser(id) {
    let response = await fetch("/api/admin/" + id);
    return await response.json();
}


/* ---------- Delete user ---------- */

async function fillModalDelete(form, modal, id) {
    let user = await getOneUser(id);
    console.log(user);
    let roles = user.roles.map(role => role.name).join(" ");
    document.getElementById('idDel').setAttribute('value', user.id);
    document.getElementById('usernameDel').setAttribute('value', user.username);
    document.getElementById('lastnameDel').setAttribute('value', user.lastname);
    document.getElementById('ageDel').setAttribute('value', user.age);
    document.getElementById('passwordDel').setAttribute('value', user.password);
    document.getElementById('userRolesDel').setAttribute('value', roles)
}
let deleteForm = document.forms["deleteUser"]
async function deleteModal(id) {
    const modal = new bootstrap.Modal(document.querySelector('#modalDelete'));
    await fillModalDelete(deleteForm, modal, id);
    deleteForm.addEventListener("submit", ev => {
        fetch("api/admin/" + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            getAllUsers();
        });
    });
}


/* ---------- Edit user ---------- */

async function fillModalEdit(form, modal, id) {
    let user = await getOneUser(id);
    let roles = user.roles.map(role => role.name).join(" ");
    console.log(roles)
    document.getElementById('idEd').setAttribute('value', user.id);
    document.getElementById('usernameEd').setAttribute('value', user.username);
    document.getElementById('lastnameEd').setAttribute('value', user.lastname);
    document.getElementById('ageEd').setAttribute('value', user.age);
    document.getElementById('passwordEd').setAttribute('value','');
    if (roles.includes("USER")) {
        document.editUserForm.rolesEd.options[1].setAttribute('selected', 'true');
    } else if (roles.includes("ADMIN")) {
        document.editUserForm.rolesEd.options[0].setAttribute('selected', 'true');
    }
}

let editForm = document.forms["editUser"]

async function editModal(id) {
    const modal = new bootstrap.Modal(document.querySelector('#modalEdit'));
    await fillModalEdit(editForm, modal, id);

    let roles = [];
    const roleSelect = document.editUserForm.rolesEd;
    function changeOption(){
        roles = [];
        const selectedOption = roleSelect.options[roleSelect.selectedIndex];
        roles.push({
            id: selectedOption.value,
            role: selectedOption.text
        })
    }
    roleSelect.addEventListener("change", changeOption);

    editForm.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("/api/admin/" + id, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editForm.id.value,
                username: editForm.username.value,
                lastname: editForm.lastname.value,
                age: editForm.age.value,
                password: editForm.password.value,
                roles: roles
            })}
        ).then(() => {
            $('#closeEdit').click();
            getAllUsers();
        });
    });
}