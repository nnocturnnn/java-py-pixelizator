package world.ucode;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;

@MultipartConfig
@WebServlet("/pix")
public class Pixelizator extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        resp.setStatus(200);
        resp.setContentType("image");
        this.process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        Part filePart = req.getPart("file");
        int pixel = Integer.parseInt(req.getParameter("pixRange"));
        String type = req.getParameter("type");
        int algo = Integer.parseInt(req.getParameter("algorithm"));
        int filter = Integer.parseInt(req.getParameter("filter"));
        BufferedImage image = ImageIO.read(filePart.getInputStream());

        Filters.applyFilters(image, filter);
        Algorithms.applyAlgorithms(image, pixel, algo);

        ImageIO.write(image, type, resp.getOutputStream());
        writeIntoDB(req);
    }

    private void writeIntoDB(HttpServletRequest req) {
        String[] reqDB = new String[4];
        reqDB[0] = req.getParameter("fileName");
        reqDB[1] = req.getParameter("pixRange");
        reqDB[2] = Algorithms.getAlgorithm(Integer.parseInt(req.getParameter("algorithm")));
        reqDB[3] = Filters.getFilter(Integer.parseInt(req.getParameter("filter")));

        RequestDB.insert(reqDB);
    }

}
