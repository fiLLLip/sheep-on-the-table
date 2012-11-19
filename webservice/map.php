<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="./wms_files/style.css" type="text/css">
    <link rel="stylesheet" href="./wms_files/style(1).css" type="text/css">
    <script src="./wms_files/OpenLayers.js"></script>
	<style type="text/css">
		
	</style>
    <script type="text/javascript">
        var lon = <?php echo $_GET['lon']; ?>;
        var lat = <?php echo $_GET['lat']; ?>;
        var zoom = 10;
        var map, layer;
		
		var markers = new OpenLayers.Layer.Markers( "Markers" );

        function init(){
            map = new OpenLayers.Map( 'map' );
            layer = new OpenLayers.Layer.WMS( "OpenLayers WMS",
                    "http://openwms.statkart.no/skwms1/wms.topo2?", {layers: 'topo2_WMS'} );
            map.addLayer(layer);

            map.setCenter(new OpenLayers.LonLat(lon, lat), zoom);
			
			map.addLayer(markers);

			var size = new OpenLayers.Size(21,25);
			var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
			var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);
			markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(lon,lat),icon));
        }
    </script>
  </head>
  <body onload="init()">
	<div id="map" class="smallmap"></div>
  </body>
</html>




