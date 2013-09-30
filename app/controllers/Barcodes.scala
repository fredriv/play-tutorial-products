package controllers

import play.api.mvc.{Action, Controller}

object Barcodes extends Controller {

  val ImageResolution = 144
  
  def barcode(ean: Long) = Action {
    import java.lang.IllegalArgumentException

    val mimeType = "image/png"
    try {
      val imageData = ean13Barcode(ean, mimeType)
      Ok(imageData).as(mimeType)
    } catch {
      case e: IllegalArgumentException =>
        BadRequest("Couldn't generate barcode. Error: " + e.getMessage)
    }
  }
  
  def ean13Barcode(ean: Long, mimeType: String): Array[Byte] = {
    import java.io.ByteArrayOutputStream
    import java.awt.image.BufferedImage
    import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
    import org.krysalis.barcode4j.impl.upcean.EAN13Bean
    
    val output = new ByteArrayOutputStream
    val canvas = new BitmapCanvasProvider(output, mimeType, ImageResolution, BufferedImage.TYPE_BYTE_BINARY, false, 0)
    
    val barcode = new EAN13Bean()
    barcode.generateBarcode(canvas, String valueOf ean)
    canvas.finish
    
    output.toByteArray
  }

}