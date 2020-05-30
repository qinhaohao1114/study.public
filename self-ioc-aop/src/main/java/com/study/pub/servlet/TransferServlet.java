package com.study.pub.servlet;

import com.study.pub.factory.BeanFactory;
import com.study.pub.pojo.Result;
import com.study.pub.service.TransferService;
import com.study.pub.utils.JsonUtils;
import com.study.pub.utils.ProxyFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="transferServlet",urlPatterns = "/transferServlet")
public class TransferServlet extends HttpServlet {
    // 1. 实例化service层对象
    private ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getObject("ProxyFactory");
    private TransferService transferService= (TransferService) proxyFactory.getProxy(BeanFactory.getObject("TransferService"));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse
            resp) throws IOException {
// 设置请求体的字符编码
        req.setCharacterEncoding("UTF-8");
        String fromCardNo = req.getParameter("fromCardNo");
        String toCardNo = req.getParameter("toCardNo");
        String moneyStr = req.getParameter("money");
        int money = Integer.parseInt(moneyStr);
        Result result = new Result();
        try {
// 2. 调⽤service层⽅法
          transferService.transfer(fromCardNo, toCardNo, money);
            result.setStatus("200");
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("201");
            result.setMessage(e.toString());
        }
// 响应
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().print(JsonUtils.object2Json(result));
    }
}
