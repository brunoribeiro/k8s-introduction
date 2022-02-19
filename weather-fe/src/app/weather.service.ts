import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  private REST_API_SERVER = window["env"]['api-url'];
  constructor(private httpClient: HttpClient) { }
  public getData(query: string) {
    return this.httpClient.get<WeatherInfoResponse>(`${this.REST_API_SERVER}?location=${query}`);
  }
}

export interface WeatherInfoResponse {
  temp: string,
  description: string,
  image: string
}
