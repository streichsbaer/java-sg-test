package testcode.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SpringUnvalidatedRedirectController {

    @RequestMapping("/redirect4")
    public ModelAndView redirect4(@RequestParam("url") String url) {
        // ruleid: taint-backend-url-redirect-modelandview
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping("/redirect5")
    public ModelAndView redirect5(@RequestParam("url") String url) {
        String view = "redirect:" + url;
        // ruleid: taint-backend-url-redirect-modelandview
        return new ModelAndView(view);
    }

    public ModelAndView normalStuff(HttpServletRequest request, HttpServletResponse response) {
        String returnURL = request.getParameter("returnURL");
        String view = "redirect:" + returnURL;
        // ruleid: taint-backend-url-redirect-modelandview
        return new ModelAndView(view);
    }

    @GetMapping("/WebWolf/landing/password-reset")
    public ModelAndView openPasswordReset(HttpServletRequest request) throws URISyntaxException {
        URI uri = new URI(request.getRequestURL().toString());
        // ruleid: taint-backend-url-redirect-modelandview
        ModelAndView modelAndView = new ModelAndView(uri);
        modelAndView.addObject("webwolfUrl", landingPageUrl);
        modelAndView.addObject("uniqueCode", StringUtils.reverse(getWebSession().getUserName()));

        modelAndView.setViewName("lessons/webwolfintroduction/templates/webwolfPasswordReset.html");
        return modelAndView;
    }

    @RequestMapping("/redirectfp")
    public String redirectfp() {
    // ok: taint-backend-url-redirect-modelandview
        return "redirect:/";
    }
}
