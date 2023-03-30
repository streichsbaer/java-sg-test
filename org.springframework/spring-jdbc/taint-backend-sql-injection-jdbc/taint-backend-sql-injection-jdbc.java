package testcode.sqli;

import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.jdbc.core.*;
import java.sql.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.object.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

// String concatenation
public class Main {
    public static void main(@PathVariable String paramName) {
        JdbcTemplate jdbc = new JdbcTemplate();
        // ruleid: taint-backend-sql-injection-jdbc
        int count = jdbc.queryForObject("select count(*) from Users where name = '"+paramName+"'", Integer.class);
    }
}

// Non-final uninitialized string
public class Main {
    private String sql;

    public static void main() {
        JdbcOperations jdbc = new JdbcOperations();
        // todoruleid: taint-backend-sql-injection-jdbc
        int count = jdbc.query(sql);
    }
}

// Prepared statement
public class Main {
    public static void main() {
        JdbcTemplate jdbc = new JdbcTemplate();
        // ok: taint-backend-sql-injection-jdbc
        int count = jdbc.queryForObject("select count(*) from Users where name = ?", Integer.class, paramName);
    }
}

// Final string
public class Main {
    private final static String sql = "select count(*) from Users";

    public static void main() {
        JdcbOperation jdbc = new JdcbOperation();
        // ok: taint-backend-sql-injection-jdbc
        int count = jdbc.query(sql);
    }
}

public class T1 {
   @PostMapping(path = "/abc")
    public String myPath(HttpServletRequest request) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader in = request.getReader()) {
            char[] buf = new char[4096];
            for (int len; (len = in.read(buf)) > 0; )
                builder.append(buf, 0, len);
        }
        String requestBody = builder.toString();
        // ruleid: taint-backend-sql-injection-jdbc
        new PreparedStatementCreatorFactory(requestBody);
        return "abc";
    }
}

public class T2 {
   @PostMapping(path = "/abc")
    public String myPath(HttpServletRequest request) throws IOException {
        String a = request.getHeader("Content-Type");
        String b = String.format("%s", a);

        // ruleid: taint-backend-sql-injection-jdbc
        new PreparedStatementCreatorFactory(b);

        return "abc";
    }
}

public class T3 {

    @PostMapping(path = "/abc")
    public String myPath(HttpServletRequest request) throws IOException {
        HttpSession sess = request.getSession();
        
        // ok: taint-backend-sql-injection-jdbc
        new PreparedStatementCreatorFactory(sess.getAttribute("myAttr").toString());

        // ruleid: taint-backend-sql-injection-jdbc
        new PreparedStatementCreatorFactory(request.getHeader("Content-Type"));
        // ok: taint-backend-sql-injection-jdbc
        new PreparedStatementCreatorFactory(request.getSession().getAttribute("myAttr2").toString());

        return "abc";
    }
}

public class T4 {
    @PostMapping(path = "/abc")
    public String myPath(HttpServletRequest request, @RequestBody String body) throws IOException {
        JdbcTemplate template = new JdbcTemplate();

        String a = request.getHeader("Content-Type");
        // ruleid: taint-backend-sql-injection-jdbc
        template.batchUpdate(a, "SELECT COUNT(*) FROM USERS WHERE age=40");

        // ruleid: taint-backend-sql-injection-jdbc
        template.execute(body);


        // fails because it can't determine the type of request.getPathInfo(). 
        // This should only happen with the JdbcTemplate.batchUpdate method, other methods such as execute will still trigger
        // todoruleid: taint-backend-sql-injection-jdbc
        template.batchUpdate(request.getPathInfo());

        // ruleid: taint-backend-sql-injection-jdbc
        template.execute(request.getPathInfo());

    }
}

@RequestMapping("/books")
@Controller
class BookController {

    private final JdbcTemplate jdbcTemplate;

    public BookController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        List<Book> books = loadBooks();

        Map<String, Object> model = new HashMap<>();
        model.put("books", books);

        return new ModelAndView("views/sql/home", model);
    }

    @RequestMapping("/detail")
    public ModelAndView detail(@RequestParam(value = "id") String id) {
        String sql = "SELECT * FROM books WHERE id=" + id;
        final Book[] book = new Book[1];
        // ruleid: taint-backend-sql-injection-jdbc
        jdbcTemplate.query(sql, (ResultSetExtractor) rs -> {
            if (rs.next())
                book[0] = new Book(rs.getLong(1), rs.getString(2), rs.getString(3));

            return null;

        });

        Map<String, Object> model = new HashMap<>();
        model.put("book", book[0]);

        return new ModelAndView("views/sql/detail", model);
    }

    @PostConstruct
    private void bootstrap() {
        initDb();

        List<Book> books = Arrays.asList(
                new Book(1L, "Moby Dick", "Herman Melville"),
                new Book(2L, "Unsichtbare Spuren", "Andreas Franz"),
                new Book(3L, "Das Paket", "Sebastian Fitzek")
        );
        saveBooks(books);
    }

    private void saveBooks(List<Book> books) {
        books.forEach(book ->
                jdbcTemplate.update("INSERT INTO books (id, `name`, author) values (?, ?, ?)",
                        book.id, book.name, book.author)
        );
    }

    private List<Book> loadBooks() {

        return jdbcTemplate.query("SELECT * FROM books", rs -> {
            List<Book> books = new LinkedList<>();

            while (rs.next()) {
                books.add(new Book(rs.getLong(1), rs.getString(2), rs.getString(3)));
            }

            return books;
        });

    }

    private void initDb() {
        jdbcTemplate.execute("CREATE TABLE books (id NUMBER, name VARCHAR(255), author VARCHAR(255))");
    }

}

@RestController
public class HelloController {
    @Autowired
    private DataSource dataSource;
    private final SimpleJdbcTemplate simpleJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public HelloController() {
        this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    private class Example {
        private String name;
        private String content;

        public Example() {}
        public String getContent() {
            return this.content;
        }
        public void setContent(String text) {
            this.content = content;
        }
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
    @GetMapping("/simpleJdbcTemplateTest")
    public String simpleJdbcTemplateTest(@RequestParam(required = false, defaultValue = "911") String id, @RequestParam String name, @RequestParam(required = false) String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.query("SELECT name, content FROM table WHERE name = '" + id + "'",
                (RowMapper<Example>) (rs, rowNum) -> {
                    var exp = new Example();
                    exp.setContent(rs.getString("content"));
                    exp.setName(rs.getString("name"));
                    return exp;
                });
        // ok: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.query("SELECT name, content FROM table WHERE id = 911",
                (RowMapper<Example>) (rs, rowNum) -> {
                    var exp = new Example();
                    exp.setContent(rs.getString("content"));
                    exp.setName(rs.getString("name"));
                    return exp;
                });
        // ruleid: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.batchUpdate("INSERT INTO table (name, content, id) VALUES (?, ? '" + id + "')",
                new ArrayList<>(Collections.singleton(new Object[]{name, content})));
        // ok: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.batchUpdate("INSERT INTO table (name, content) VALUES (?, ?)",
                new ArrayList<>(Collections.singleton(new Object[]{name, content})));
        // ok: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.queryForList("SELECT name, content FROM table WHERE id = ?", id);
        // ruleid: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.update("UPDATE table SET content = ? WHERE name = '" + name + "'", content);
        // ok: taint-backend-sql-injection-jdbc
        simpleJdbcTemplate.update("UPDATE table SET content = ? WHERE name = ?", content, name);
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/jdbcTemplateTest")
    public String jdbcTemplateTest(@RequestParam(required = false, defaultValue = "911") String id, @RequestParam String name, @RequestParam(required = false) String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        jdbcTemplate.query("SELECT name, content FROM table WHERE name = '" + id + "'",
                (rs, rowNum) -> {
                    var exp = new Example();
                    exp.setContent(rs.getString("content"));
                    exp.setName(rs.getString("name"));
                    return exp;
                });
        // ok: taint-backend-sql-injection-jdbc
        jdbcTemplate.query("SELECT name, content FROM table WHERE id = 911",
                (rs, rowNum) -> {
                    var exp = new Example();
                    exp.setContent(rs.getString("content"));
                    exp.setName(rs.getString("name"));
                    return exp;
                });
        // ruleid: taint-backend-sql-injection-jdbc
        jdbcTemplate.batchUpdate("INSERT INTO table (name, content, id) VALUES (?, ? '" + id + "')",
                // ok: taint-backend-sql-injection-jdbc
                new ArrayList<>(Collections.singleton(new Object[]{name, content})));
        // ok: taint-backend-sql-injection-jdbc
        jdbcTemplate.batchUpdate("INSERT INTO table (name, content) VALUES (?, ?)",
                // ok: taint-backend-sql-injection-jdbc
                new ArrayList<>(Collections.singleton(new Object[]{name, content})));
        // ok: taint-backend-sql-injection-jdbc
        jdbcTemplate.queryForList("SELECT name, content FROM table WHERE id = ?", id);
        // ruleid: taint-backend-sql-injection-jdbc
        jdbcTemplate.update("UPDATE table SET content = ? WHERE name = '" + name + "'", content);
        // ok: taint-backend-sql-injection-jdbc
        jdbcTemplate.update("UPDATE table SET content = ? WHERE name = ?", content, name);
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlFunctionTest")
    public String sqlFunctionTest(@RequestParam String name, @RequestParam String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        SqlFunction<Example> sf = new SqlFunction<>(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'");
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");
        sf.runGeneric();
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlFunctionTest01")
    public String sqlFunctionTest01(@RequestParam String name, @RequestParam String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        SqlFunction<> sf = new SqlFunction<Example>(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'");
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");
        sf.runGeneric();
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlFunctionTest02")
    public String sqlFunctionTest02(@RequestParam String name, @RequestParam String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        SqlFunction sf = new SqlFunction(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'");
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");
        sf.runGeneric();
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlCallTest")
    public String sqlCallTest(@RequestParam String name, @RequestParam String content) {
        // todoruleid: taint-backend-sql-injection-jdbc
        SqlCall sf = new SqlCall(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'") {};
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlQueryTest")
    public String sqlQueryTest(@RequestParam String name, @RequestParam String content) {
        // todoruleid: taint-backend-sql-injection-jdbc
        SqlQuery<> sf = new SqlQuery<Example>(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'") {
            @Override
            protected RowMapper<Example> newRowMapper(Object[] objects, Map<?, ?> map) {
                return null;
            }
        };
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/mappingSqlQueryWithParametersTest")
    public String mappingSqlQueryWithParametersTest(@RequestParam String name, @RequestParam String content) {
        // todoruleid: taint-backend-sql-injection-jdbc
        MappingSqlQueryWithParameters<Example> sf = new MappingSqlQueryWithParameters(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'") {
            @Override
            protected Object mapRow(ResultSet resultSet, int i, Object[] objects, Map map) throws SQLException {
                return null;
            }
        };
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/mappingSqlQueryTest")
    public String mappingSqlQueryTest(@RequestParam String name, @RequestParam String content) {
        // todoruleid: taint-backend-sql-injection-jdbc
        MappingSqlQuery<Example> sf = new MappingSqlQuery<>(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'") {
            @Override
            protected Example mapRow(ResultSet resultSet, int i) throws SQLException {
                return null;
            }
        };
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/updatableSqlQueryTest")
    public String updatableSqlQueryTest(@RequestParam String name, @RequestParam String content) {
        // todoruleid: taint-backend-sql-injection-jdbc
        UpdatableSqlQuery<Example> sf = new UpdatableSqlQuery<>(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'") {
            @Override
            protected Example updateRow(ResultSet resultSet, int i, Map map) {
                return null;
            }
        };
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlUpdateTest")
    public String sqlUpdateTest(@RequestParam String name, @RequestParam String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        SqlUpdate sf = new SqlUpdate(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'");
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");
        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/batchSqlUpdateTest")
    public String batchSqlUpdateTest(@RequestParam String name, @RequestParam String content) {
        // ruleid: taint-backend-sql-injection-jdbc
        BatchSqlUpdate sf = new BatchSqlUpdate(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'");
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/storedProcedureTest")
    public String StoredProcedureTest(@RequestParam String name, @RequestParam String content) {
        // todoruleid: taint-backend-sql-injection-jdbc
        StoredProcedure sf = new StoredProcedure(dataSource, "SELECT name, content FROM table WHERE content = '" + content + "'") {};
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/genericStoredProcedureTest")
    public String genericStoredProcedureTest(@RequestParam String name, @RequestParam String content) {
        GenericStoredProcedure sf = new GenericStoredProcedure();
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/sqlOperationTest")
    public String sqlOperationTest(@RequestParam String name, @RequestParam String content) {
        SqlOperation sf = new SqlOperation() {};
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }

    @GetMapping("/genericSqlQueryTest")
    public String genericSqlQueryTest(@RequestParam String name, @RequestParam String content) {
        GenericSqlQuery<Example> sf = new GenericSqlQuery<>();
        // ruleid: taint-backend-sql-injection-jdbc
        sf.setSql("SELECT name, content FROM table WHERE name = '" + name + "'");

        return "Greetings from Spring Boot! ";
    }
}