package org.sda.librarymanagement.controller;

import java.util.List;

import org.sda.librarymanagement.entity.Client;
import org.sda.librarymanagement.entity.dto.ClientDTO;
import org.sda.librarymanagement.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/library")
public class ClientController {
	@Autowired
	private ClientService clientService;

	@PostMapping("/client")
	public ResponseEntity<Void> addClient(@RequestBody ClientDTO clientDTO, UriComponentsBuilder builder) {
		Client client = clientService.convertFromDTOToEntity(clientDTO);
		clientService.saveClient(client);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/client").buildAndExpand(client.getClientId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/client/{id}")
	public ResponseEntity<Client> getClientById(@PathVariable("id") Long id) {
		Client client = clientService.getOneClientById(id);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	@GetMapping("/clients")
	public ResponseEntity<List<Client>> getAllClients() {
		List<Client> client = (List<Client>) clientService.getAllClients();
		return new ResponseEntity<List<Client>>(client, HttpStatus.OK);
	}

	@PutMapping("/client/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
		Client client = clientService.convertFromDTOToEntity(clientDTO);

		clientService.updateClient(id, client);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	@DeleteMapping("/client/{id}")
	public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id) {
		clientService.deleteClient(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
