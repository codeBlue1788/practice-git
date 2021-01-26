

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import shopping.Food;

/**
 * Servlet implementation class fShopServlet
 */
public class fShopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fShopServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		Vector<Food> buyList = (Vector<Food>) session.getAttribute("shoppingcart");
		String action = request.getParameter("action");
		
		if(!action.equals("CHECKOUT")) {
			if(action.equals("DELETE")) {
				String del = request.getParameter("del");
				int d = Integer.parseInt(del);
				buyList.removeElementAt(d);
				
			}else if(action.equals("ADD")) {
				boolean match = false;
				Food afood = getFood(request);
				
				if(buyList==null) {
					buyList = new Vector<Food>();
					buyList.add(afood);
				}else if(buyList!=null){
					for(int i = 0; i<buyList.size(); i++) {
						Food food = buyList.get(i);
						
						if(food.getCom_Name().equals(afood.getCom_Name())) {
							food.setQuantity(food.getQuantity() + afood.getQuantity());
							buyList.setElementAt(food, i);
							match = true;
						}
						
					}
					
					if(!match) {
						buyList.add(afood);
					}
				}
			}
			session.setAttribute("shoppingcart", buyList);
			String url = "/FShop.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
			
		}else if(action.equals("CHECKOUT")) {
			int total = 0;
			for(int i = 0 ; i<buyList.size();i++) {
				Food order = buyList.get(i);
				int price = order.getCom_Price();
				int quantity = order.getQuantity();
				total += (price * quantity);
			}
			
			String amount = String.valueOf(total);
			request.setAttribute("amount", amount);
			String url = "/FCheckout.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request,response);
		}
		
		
		
		
	}
	
	private Food getFood(HttpServletRequest request) {
		String name = request.getParameter("com_Name");
		String pic = request.getParameter("com_Pic");
		String content = request.getParameter("com_Content");
		String unit = request.getParameter("com_Unit");
		String price = request.getParameter("com_Price");
		String quantity = request.getParameter("quantity");
		
		Food fd = new Food();
		fd.setCom_Name(name);
		fd.setCom_Pic(pic);
		fd.setCom_Content(content);
		fd.setCom_Unit(unit);
		fd.setCom_Price(Integer.parseInt(price));
		fd.setQuantity(Integer.parseInt(quantity));
	
		return fd;
	}

	
	
	
	
	
}
