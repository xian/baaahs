require 'rubygems'
require 'bundler'
require 'sinatra'
require "sinatra/compass"
require "sinatra/reloader" if development?
require "sinatra/activerecord"
require "action_view"
require 'erubis'

require 'pathname'

# Bundler.require

$: << File.expand_path('../app', __FILE__)

require 'models/asset'
require 'models/scan'

class Helper
  include ActionView::Helpers::JavaScriptHelper

  def self.escape_js(text)
    @instance ||= self.new
    return @instance.escape_javascript(text)
  end
end

module BaaahsOrg
  class App < Sinatra::Application
    configure do
      disable :method_override
      disable :static

      set :sessions,
          :httponly => true,
          :secure => production?,
          :expire_after => 31557600, # 1 year
          :secret => ENV['SESSION_SECRET']
    end


    public_html = Pathname('public')

# redirects to keep!
    get('/drive') { redirect "https://drive.google.com/drive/folders/0B_TasILTM6TWa18zdHdmNHpUYzg" }
    get('/pspride') { redirect "/psp/" }
    get('/join') { redirect "http://goo.gl/forms/XUvltyxql2" }

# old URLs to support for a while!
    get('/shifts') { redirect "http://www.volunteerspot.com/login/entry/375755452038" } # todo kill after 20151201

# routes!

    get '/' do
      send_file public_html.join('index.html')
    end

    get '/a/:tag' do
      tag = params[:tag]
      asset = ::Asset.find_or_initialize_by tag: tag
      is_new = asset.new_record?
      asset.save! if asset.new_record?

      puts({id: asset.id, tag: asset.tag, name: asset.name}.to_json)

      scan = ::Scan.create!(
          asset: asset,
      )

      erb :asset, locals: {
          tag: tag,
          asset: asset,
          is_new: is_new,
          scan: scan,
      }
    end

    post '/a/:tag' do
      tag = params[:tag]
      p params
      body = JSON.parse(request.body.read)
      p body
      asset = ::Asset.find_by_tag tag

      scan_params = body["scan"]
      if scan_params
        scan = asset.scans.find_by_id scan_params["id"]
        puts "Scan: #{scan.to_s}"
        if scan
          {
              latitude: "latitude",
              longitude: "longitude",
              accuracy: "accuracy",
              altitude: "altitude",
              altitude_accuracy: "altitudeAccuracy",
          }.each do |k, v|
            scan.__send__("#{k}=", scan_params[v]) if scan_params[v]
          end
          scan.save! if scan.changed?
        end
      end

      asset.name = body["name"] if body["name"]
      # asset.user = ::User.find_by_id if params[:name]
      asset.save! if asset.changed?
    end

    get '/*' do |file|
      file = file.gsub(/\.\./, '')

      if public_html.join("#{file}.html").file?
        send_file public_html.join("#{file}.html")
      elsif public_html.join(file).directory? && public_html.join(file, 'index.html').file?
        if file =~ /\/$/
          send_file public_html.join(file, 'index.html')
        else
          redirect "#{file}/"
        end
      else
        pass
      end
    end

    not_found do
      send_file public_html.join('404.html')
    end

    use Rack::Deflater
  end
end