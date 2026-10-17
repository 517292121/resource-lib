<%@ page language="java" pageEncoding="UTF-8"%>  
<%@ page contentType="image/jpeg"  import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*" %>  
<%!  
    /** 
    * 生成随机着色 
    */  
    public Color getColor(){  
        Random random = new Random();  
        int r = random.nextInt(256);//0-255  
        int g = random.nextInt(256);  
        int b = random.nextInt(256);  
        return new Color(r,g,b);  
    }  
      
    //定义验证码的随机字符串  
    String randomStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";  
      
    /** 
    * 生成随机字符 
    */  
    public String getStr(){  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for(int i=0;i<4;i++){  
            sb.append(randomStr.charAt(random.nextInt(randomStr.length())));//每次提取一个随机字符  
        }  
        return sb.toString();  
    }  
%>  
<%  
    //设置响应头部信息告诉浏览器不缓存图片  
    response.setHeader("pragma", "mo-cache");  
    response.setHeader("cache-control", "no-cache");  
    response.setDateHeader("expires", 0);  
       
    BufferedImage image = new BufferedImage(100,40,BufferedImage.TYPE_INT_RGB);  
       
    Graphics g = image.getGraphics();  
    g.setColor(new Color(255,255,255));  
    g.fillRect(0,0,100,40);  
      
    Random random = new Random();   
    
       
       
    g.setFont(new Font("宋体", Font.ROMAN_BASELINE,24));  
    g.setColor(Color.BLACK);  
    String randomCode = getStr();  
    
    g.translate(13,0);
    for(int i=0;i<randomCode.length();i++){  
        g.drawString(randomCode.charAt(i)+"",random.nextInt(5),28);//sb.append(randomCode.charAt(i));  
        g.translate(20,0);  
    }  
       
    //将验证码放入Session中  
    session.setAttribute("REGIMGCODE",randomCode);  
      
    //通过流将验证码图片输出   
    ImageIO.write(image,"jpeg",response.getOutputStream());  
    out.clear();  
    out = pageContext.pushBody();  
%>
