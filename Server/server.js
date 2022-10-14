let express = require("express");
let mysql = require("mysql");
const { md5 } = require("./md5");

let connection = mysql.createConnection({
  host: "162.248.100.135",
  user: "serverUBase",
  password: "6L)ZgIOUelOXznqH",
  database: "ubase",
});

const bodyParser = require("body-parser");
const router = express.Router();
const app = express();

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

const port = 80;
const auth = "hxoTR9Z5t7feByHoBvh3L2joe7LxrLH1DxeTaMYJ";
const successMessage = {
  status: "success",
  message: null,
};
const errorMessage = {
  status: "error",
  code: 0,
  message: "",
};

// ------------------- Auth and Register ------------------- //
router.post("/api/register", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const name = req.body.name;
    const login = req.body.login;
    const password = req.body.password;
    if (name == undefined || login == undefined || password == undefined) {
      errorMessage.message = "Invalid body.";
      errorMessage.code = 1;
      return res.send(JSON.stringify(errorMessage));
    }
    connection.query(
      "SELECT * FROM `users` WHERE login='" + login + "'",
      (error, results, fields) => {
        if (error) {
          return console.error(error.message);
        }
        if (results.length === 0) {
          connection.query(`INSERT INTO users(id, name, login, password)
            VALUES(null, '${name}', '${login}', '${md5(password)}')`);
          return res.send(JSON.stringify(successMessage));
        } else {
          errorMessage.code = 2;
          errorMessage.message = "Registration error, login already exists.";
          return res.send(JSON.stringify(errorMessage));
        }
      }
    );
  }
});

router.post("/api/login", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const login = req.body.login;
    const password = req.body.password;
    if (login == undefined || password == undefined) {
      errorMessage.message = "Invalid body.";
      errorMessage.code = 1;
      return res.send(JSON.stringify(errorMessage));
    }
    connection.query(
      "SELECT * FROM `users` WHERE login='" + login + "'",
      (error, results, fields) => {
        if (error) {
          return console.error(error.message);
        }
        if (results.length === 0) {
          errorMessage.message = "Auth error, login not found.";
          errorMessage.code = 2;
          return res.send(JSON.stringify(errorMessage));
        } else {
          if (md5(password) !== results[0]["password"]) {
            errorMessage.message = "Auth error, password is incorrect.";
            errorMessage.code = 3;
            return res.send(JSON.stringify(errorMessage));
          } else {
            return res.send(JSON.stringify(successMessage));
          }
        }
      }
    );
  }
});

// ------------------- Students (get all, get from departament, get from name, edit, delete) ------------------- /
router.post("/api/students/getall", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const login = req.body.login;
    connection.query(
      "SELECT * FROM students WHERE login='" + login + "'",
      (error, results, fields) => {
        successMessage.message = results;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/students/get", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const departament = req.body.name;
    const login = req.body.login;
    connection.query(
      "SELECT * FROM students WHERE departament='" +
        departament +
        "' AND login='" +
        login +
        "'",
      (error, results, fields) => {
        successMessage.message = results;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/student/get", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const name = req.body.name;
    const login = req.body.login;
    connection.query(
      "SELECT * FROM students WHERE name='" +
        name +
        "' AND login='" +
        login +
        "'",
      (error, results, fields) => {
        successMessage.message = results;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/student/checkDate", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const date = req.body.date;
    const login = req.body.login;
    connection.query(
      "SELECT nexist FROM students WHERE id='" +
        id +
        "' AND login='" +
        login +
        "'",
      (error, results, fields) => {
        let check = JSON.parse(results[0].nexist).dates.includes(date);
        successMessage.message = check;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/student/editDates", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const param = req.body.param;
    const id = req.body.id;
    const date = req.body.date;
    const login = req.body.login;
    if (param == "add") {
      connection.query(
        "SELECT * FROM students WHERE id='" +
          id +
          "' AND login='" +
          login +
          "'",
        (error, results, fields) => {
          let nexist = JSON.parse(results[0].nexist);
          nexist.dates.push(date);
          connection.query(
            "UPDATE students SET nexist='" +
              JSON.stringify(nexist) +
              "' WHERE id='" +
              id +
              "' AND login='" +
              login +
              "'",
            (err, result) => {
              successMessage.message = result;
              return res.send(JSON.stringify(successMessage));
            }
          );
        }
      );
    } else {
      connection.query(
        "SELECT * FROM students WHERE id='" +
          id +
          "' AND login='" +
          login +
          "'",
        (error, results, fields) => {
          let nexist = JSON.parse(results[0].nexist);
          nexist.dates.splice(nexist.dates.indexOf(date), 1);
          connection.query(
            "UPDATE students SET nexist='" +
              JSON.stringify(nexist) +
              "' WHERE id='" +
              id +
              "' AND login='" +
              login +
              "'",
            (err, result) => {
              successMessage.message = result;
              return res.send(JSON.stringify(successMessage));
            }
          );
        }
      );
    }
  }
});

router.post("/api/student/add", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const name = req.body.name;
    const faculty = req.body.faculty;
    const departament = req.body.departament;
    const date = req.body.date;
    const nexist = { dates: [] };
    const login = req.body.login;
    connection.query(
      "INSERT INTO `students`(`id`, `name`, `faculty`, `departament`, `date`, `nexist`, `login`) VALUES (null, '" +
        name +
        "', '" +
        faculty +
        "', '" +
        departament +
        "', '" +
        date +
        "', '" +
        JSON.stringify(nexist) +
        "', '" +
        login +
        "')",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/student/edit", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const newName = req.body.newname;
    const newDate = req.body.newdate;
    const login = req.body.login;
    connection.query(
      "UPDATE students SET name='" +
        newName +
        "', date='" +
        newDate +
        "' WHERE id='" +
        id +
        "' AND login='" +
        login +
        "'",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/student/delete", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const login = req.body.login;
    connection.query(
      "DELETE FROM `students` WHERE id='" + id + "' AND login='" + login + "'",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

// ------------------- Departaments (get, add, edit, delete) ------------------- /
router.post("/api/departament/get", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const faculty = req.body.name;
    const login = req.body.login;
    connection.query(
      "SELECT * FROM departaments WHERE faculty='" +
        faculty +
        "' AND login='" +
        login +
        "'",
      (error, results, fields) => {
        successMessage.message = results;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/departaments/get", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const login = req.body.login;
    connection.query(
      "SELECT * FROM departaments WHERE login='" + login + "'",
      (error, results, fields) => {
        successMessage.message = results;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/departament/add", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const faculty = req.body.faculty;
    const departament = req.body.departament;
    const login = req.body.login;
    connection.query(
      "INSERT INTO `departaments`(`id`, `name`, `faculty`, `login`) VALUES (null,'" +
        departament +
        "', '" +
        faculty +
        "', '" +
        login +
        "')",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/departament/edit", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const newName = req.body.newname;
    const login = req.body.login;
    connection.query(
      "UPDATE departaments SET name='" +
        newName +
        "' WHERE id='" +
        id +
        "' AND login='" +
        login +
        "'",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/departament/delete", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const login = req.body.login;
    connection.query(
      "DELETE FROM `departaments` WHERE id='" +
        id +
        "' AND login='" +
        login +
        "'",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

// ------------------- Faculty (get, edit, delete) ------------------- /

router.post("/api/faculty/get", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const login = req.body.login;
    connection.query(
      "SELECT * FROM faculties WHERE login='" + login + "'",
      (error, results, fields) => {
        successMessage.message = results;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/faculty/add", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const name = req.body.name;
    const login = req.body.login;
    connection.query(
      "INSERT INTO `faculties`(`id`, `name`, `login`) VALUES (null,'" +
        name +
        "', '" +
        login +
        "')",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/faculty/edit", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const newName = req.body.newname;
    const login = req.body.login;
    connection.query(
      "UPDATE faculties SET name='" +
        newName +
        "' WHERE id='" +
        id +
        "' AND login='" +
        login +
        "'",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

router.post("/api/faculty/delete", (req, res) => {
  if (req.headers.authorization != auth) {
    errorMessage.message = "Authorization key error.";
    return res.send(JSON.stringify(errorMessage));
  } else {
    const id = req.body.id;
    const login = req.body.login;
    connection.query(
      "DELETE FROM `faculties` WHERE id='" + id + "' AND login='" + login + "'",
      (err, result) => {
        successMessage.message = result;
        return res.send(JSON.stringify(successMessage));
      }
    );
  }
});

app.listen(port, () => {
  console.log("UBase Server is running.");
});

app.use("/", router);
