import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {

        //fazer uma conexão HTTP e buscar os top 250 filmes
		String url = "https://raw.githubusercontent.com/alexfelipe/imersao-java/json/top250.json";
		URI endereco = URI.create(url);		
		//fazer client
		var client = HttpClient.newHttpClient();
		//fazer request
		var request = HttpRequest.newBuilder(endereco).GET().build();
		//fazer response
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		String body = response.body();
		
		//extrair, ou parsear, só os dados que interessam (título, poster, classificação/rating)
		var parser = new JsonParser();
		List<Map<String, String>> listaDeFilmes = parser.parse(body);
		
		//exibir e manipular os dados
		var geradora = new GeradoraDeFigurinhas();
		for (Map<String,String> filme : listaDeFilmes) {

			String urlImagem = filme.get("image");
			String titulo = filme.get("title");
			String ano = filme.get("year");
			String classificacao = filme.get("imDbRating");

			InputStream inputStream = new URL(urlImagem).openStream();
			String nomeArquivo = titulo + ".png";

			geradora.cria(inputStream, nomeArquivo);

			System.out.println("\u001b[1mTítulo: " + titulo);
			System.out.println("\u001b[mPoster: " + urlImagem);
			System.out.println("Ano: " + ano);
			System.out.println("\u001b[33m\u001b[45m\u001b[3mNota IMDB: " + classificacao + "\u001b[m");
			System.out.println();
		}

    }
}
